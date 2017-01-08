import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;

public class MainClass {

	static int min = 5;
	static int max = 1000;
	static int histBinsLength = 20;
	static int histBinsOrient = 20;
	static boolean print = false;
	static int lowThreshold = 33;
	static int highThreshold = 100;

	public static void main(String[] args) throws IOException {

		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

		int selection = 3; //Integer.parseInt(args[0]);
		
		switch (selection){
			case 1:
				//preprocessing of source folder
				PreprocessImage ppi = new PreprocessImage("hsl");
				ppi.convert();
				break;
			case 2:
				//create the file containing edge and orientation count and category of all images
				createHistFile("enhanced_rgb");
				break;
			case 3:
				
				//run tests using the created hist-file and images in the specified folder.
				//also specify category
				runTests("test", "1");
		}
	}



	private static void createHistFile(String inputImagePath) throws IOException{

		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		EdgeHistogram eh;
		BufferedWriter output = new BufferedWriter(new FileWriter("histograms\\histograms_" + min + "-" + max + "_" + histBinsLength + ".txt"));

		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
					eh = new EdgeHistogram(inputImagePath + "\\" + currFolder + "\\" + currImage, 10, 1000, highThreshold, lowThreshold);
					eh.calcHistogram();
					output.append((eh.evaluatelength(min, max, histBinsLength, print)));
					output.append(",");

					output.append((eh.evaluateOrientation(histBinsOrient, print)));
					output.append("," + writeCategory(Integer.parseInt(currFolder)));
					output.append("\n");
				}
			}
		}
		output.flush();
		output.close();
	}

	private static void runTests(String testFolder, String category) throws IOException{

		String[] tempHistValues;
		String[] testImages = new File(testFolder).list();
		ArrayList<Double> vector = new ArrayList<>();
		int correctClass = 0;
		EdgeHistogram eh;
		String foundCategory = "";
		KNearestNeighbour a = new KNearestNeighbour("histograms\\histograms_" + min + "-" + max + "_" + histBinsLength + ".txt");

		for(String currTestImage: testImages){
			//create feature vector for test image:
			eh = new EdgeHistogram(testFolder + "\\" + currTestImage, 10, 1000, highThreshold, lowThreshold);
			eh.calcHistogram();
			tempHistValues = eh.evaluatelength(min, max, histBinsLength, print).split(",");
			for(int i = 0; i < tempHistValues.length; i++){
				vector.add(Double.parseDouble(tempHistValues[i]));
			}
			
			tempHistValues = eh.evaluateOrientation(histBinsOrient, print).split(",");
			for(int i = 0; i < tempHistValues.length; i++){
				vector.add(Double.parseDouble(tempHistValues[i]));
			}

			//execute KNN:
			foundCategory = a.getKnnCategory(new FeatureVector(vector,category),5);
			if(foundCategory.equals(category)){
				correctClass++;
			}
			System.out.println("Class: " + foundCategory);
		}
		System.out.println((double)correctClass / (double)testImages.length * 100 + "%");
	}

	
	private static String writeCategory(int cat){
		switch(cat){
		case 1:
		case 2:
			return "1";
		case 3:
		case 4:
			return "2";
		case 5:
		case 6:
			return "3";
		default:
			return "error: Invalid Category";
		}
	}
	
	
	
}