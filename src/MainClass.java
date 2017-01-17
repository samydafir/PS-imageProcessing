import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Core;

public class MainClass {

	static String testPath = "hsv";
	static int min = 5;
	static int max = 800;
	static int histBinsLength = 50;
	static int histBinsOrient = 20;
	static boolean print = false;
	static int lowThreshold = 33;
	static int highThreshold = 100;
	static int selection = 3;
	static int k = 5;
	static boolean evalOrientation = true;

	public static void main(String[] args) throws IOException {

		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		switch (selection){
			case 1:
				//preprocessing of source folder
				String[] colorModes = {/*"lab", "yuv", "hsl", "hsv", */"double_enhanced_rgb"};
				PreprocessImage ppi = new PreprocessImage("enhanced_rgb/");
				ppi.convert(colorModes);
				break;
			case 2:
				//create the file containing edge and orientation count and category of all images
				createHistFile(testPath);
				break;
			case 3:
				
				//run tests using the created hist-file and images in the specified folder.
				//also specify category
				double[] result;
				double totalImages = 0;
				double totalPercent = 0;
				for(int i = 1; i <= 6; i++){
					result = runTests(testPath + "/" + i, writeCategory(i));
					totalImages += result[0];
					totalPercent += (result[1] * result[0]);
					System.out.println("Folder" + i + ": " +result[1]);
				}
				System.out.println("Total: " + totalPercent/totalImages);
				
		}
	}



	private static void createHistFile(String inputImagePath) throws IOException{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("images/patientmapping.csv")));
		HashMap<String, Integer> imageToPatient = new HashMap<>();
		String currFile = reader.readLine();
		
		while (currFile != null){
			String[] line = currFile.split(";");
			imageToPatient.put(line[0], Integer.parseInt(line[1]));
			currFile = reader.readLine();
		}
		reader.close();

		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		EdgeHistogram eh;
		BufferedWriter output = new BufferedWriter(new FileWriter("histograms\\histograms_" + min + "-" + max + "_" + histBinsLength + ".txt"));
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
					eh = new EdgeHistogram(inputImagePath + "\\" + currFolder + "\\" + currImage, 1000, highThreshold, lowThreshold);
					eh.calcHistogram();
					output.append((eh.evaluatelength(min, max, histBinsLength, print)));
					if(evalOrientation){
						output.append(",");
						output.append((eh.evaluateOrientation(histBinsOrient, print)));
					}
					output.append("," + writeCategory(Integer.parseInt(currFolder)));
					output.append(",");
					output.append(imageToPatient.get(currImage) + "");
					output.append("\n");
				}
			}
		}
		output.flush();
		output.close();
	}

	private static double[] runTests(String testFolder, int category) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("images/patientmapping.csv")));
		HashMap<String, Integer> imageToPatient = new HashMap<>();
		String currFile = reader.readLine();
		
		while (currFile != null){
			String[] line = currFile.split(";");
			imageToPatient.put(line[0], Integer.parseInt(line[1]));
			currFile = reader.readLine();
		}
		reader.close();
		
		String[] tempHistValues;
		String[] testImages = new File(testFolder).list();
		ArrayList<Double> vector = new ArrayList<>();
		int correctClass = 0;
		EdgeHistogram eh;
		int foundCategory = -1;
		KNearestNeighbour a = new KNearestNeighbour("histograms\\histograms_" + min + "-" + max + "_" + histBinsLength + ".txt");

		for(String currTestImage: testImages){
			vector = new ArrayList<>();
			//create feature vector for test image:
			eh = new EdgeHistogram(testFolder + "\\" + currTestImage, 1000, highThreshold, lowThreshold);
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
			foundCategory = a.getKnnCategory(new FeatureVector(vector,category,imageToPatient.get(currTestImage)),k);
			if(foundCategory == category){
				correctClass++;
			}
			//System.out.println("Class: " + foundCategory);
		}
		return new double[] {testImages.length, (double)correctClass / (double)testImages.length * 100};
	}

	
	private static int writeCategory(int cat){
		switch(cat){
		case 1:
		case 2:
			return 1;
		case 3:
		case 4:
			return 2;
		case 5:
		case 6:
			return 3;
		default:
			return -1;
		}
	}
	
	
	
}