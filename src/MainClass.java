import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Core;

public class MainClass {

	static int min = 100;
	static int max = 1000;
	static int histBins = 10;
	static boolean print = false;

	public static void main(String[] args) throws IOException {
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

		/* uncomment to enable preprocessing of source folder
		PreprocessImage ppi = new PreprocessImage("images");
		ppi.convert();
		*/
		
		//uncomment to create the file containing edge count and category of all images
		//createHistFile("images");

		//run tests using the created hist-file and images in the specified folder.
		//also specify category
		runTests("test", "6");

	}
	
	
	
	private static void createHistFile(String inputImagePath) throws IOException{
		
		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		EdgeHistogram eh;
		BufferedWriter output = new BufferedWriter(new FileWriter("histograms\\histograms_" + min + "-" + max + "_" + histBins + ".txt"));
		int cat;
		
		cat = 1;
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
					eh = new EdgeHistogram(inputImagePath + "\\" + currFolder + "\\" + currImage, 5, 1000, 10, 1);
					eh.calcHistogram();
					output.append((eh.evaluate(min, max, histBins, print)));
					output.append("," + cat);
					output.append("\n");
				}
				cat++;
			}
		}
		output.flush();
		output.close();
	}

	private static void runTests(String testFolder, String category) throws IOException{
		
		String[] histValues;
		String[] testImages = new File(testFolder).list();
		ArrayList<Double> vector = new ArrayList<>();
		int correctClass = 0;
		EdgeHistogram eh;
		String foundCategory = "";
		KNearestNeighbour a = new KNearestNeighbour("histograms\\histograms_" + min + "-" + max + "_" + histBins + ".txt");
		
		for(String currTestImage: testImages){
			//create feature vector for test image:
			eh = new EdgeHistogram(testFolder + "\\" + currTestImage, 5, 1000, 10, 1);
			eh.calcHistogram();
			histValues = eh.evaluate(min, max, histBins, print).split(",");
			for(int i = 0; i < histValues.length; i++){
				vector.add(Double.parseDouble(histValues[i]));
			}
		
			//execute KNN:
			foundCategory = a.getKnnCategory(new FeatureVector(vector,category),7);
			if(foundCategory.equals(category)){
				correctClass++;
			}
			//System.out.println("Class: " + foundCategory);
		}
		System.out.println((double)correctClass / (double)testImages.length * 100 + "%");
	}

}





