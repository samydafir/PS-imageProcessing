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

//	static String testPath = "hsv";
//	static int min = 5;
//	static int max = 800;
//	static int histBinsLength = 50;
//	static int histBinsOrient = 20;
//	static boolean print = false;
//	static int lowThreshold = 33;
//	static int highThreshold = 100;
//	static int selection = 2;
//	static int k = 5;
//	static boolean evalOrientation = true;
	
	static String testPath = "enhanced_rgb";
	static int min = 100;
	static int max = 1500;
	static int histBinsLength = 50;
	static int histBinsOrient = 20;
	static boolean print = false;
	static int lowThreshold = 33;
	static int highThreshold = 100;
	static int selection = 2;
	static int k = 5;
	static boolean evalOrientation = true;

	public static void main(String[] args) throws IOException {

		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		if (args.length > 0) {
			selection = Integer.parseInt(args[0]);
			testPath = args[1];
			min = Integer.parseInt(args[2]);
			max = Integer.parseInt(args[3]);
			histBinsLength = Integer.parseInt(args[4]);
			histBinsOrient = Integer.parseInt(args[5]);
			lowThreshold = Integer.parseInt(args[6]);
			highThreshold = Integer.parseInt(args[7]);
			k = Integer.parseInt(args[8]);
		}
		
		
		switch (selection){
			case 1:
				//preprocessing of source folder
				String[] colorModes = {"lab", "yuv", "hsl", "hsv", "double_enhanced_rgb"};
				PreprocessImage ppi = new PreprocessImage("enhanced_rgb");
				ppi.convert(colorModes);
				break;
			case 2:
				//create the file containing edge and orientation count and category of all images
				createHistFile(testPath);
				break;
			case 3:
				
				//run tests using the created hist-file and images in the specified folder.
				//also specify category
				
				BufferedWriter output = new BufferedWriter(new FileWriter(
						"histograms/eval_histograms_" 
						+ testPath + "_" 
						+ min + "-" + max + "_" 
						+ histBinsLength + "_" + histBinsOrient + "_" 
						+ lowThreshold + "_" + highThreshold + "_"
						+ k + ".txt"));
				
				double[] result;
				double totalImages = 0;
				double totalPercent = 0;
				String[] foldersArr = new String[3];
				for(int i = 1; i <= 3; i++){
					result = runTests(testPath + "/" + i, writeCategory(i));
					totalImages += result[0];
					totalPercent += (result[1] * result[0]);
					foldersArr[i-1] = "Folder" + i + ": " + result[1] + "\n";
				}
				output.append(totalPercent/totalImages + "\n");
				for (String s : foldersArr) {
					output.append(s);
				}
				output.append("Total: " + totalPercent/totalImages);
				output.flush();
				output.close();
				
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
		BufferedWriter output = new BufferedWriter(new FileWriter(
				"histograms/histograms_" 
				+ testPath + "_" 
				+ min + "-" + max + "_" 
				+ histBinsLength + "_" + histBinsOrient + "_" 
				+ lowThreshold + "_" + highThreshold + "_"
				+ k + ".txt"));
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "/" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currImage: new File(inputImagePath + "/" + currFolder).list()){
					eh = new EdgeHistogram(inputImagePath + "/" + currFolder + "/" + currImage, 1000, highThreshold, lowThreshold);
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
		KNearestNeighbour a = new KNearestNeighbour(
				"histograms/histograms_" 
				+ testPath + "_" 
				+ min + "-" + max + "_" 
				+ histBinsLength + "_" + histBinsOrient + "_" 
				+ lowThreshold + "_" + highThreshold + "_"
				+ k + ".txt");

		for(String currTestImage: testImages){
			vector = new ArrayList<>();
			//create feature vector for test image:
			eh = new EdgeHistogram(testFolder + "/" + currTestImage, 1000, highThreshold, lowThreshold);
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
		case 5:
			return 2;
		case 6:
			return 3;
		default:
			return -1;
		}
	}
	
	
	
}