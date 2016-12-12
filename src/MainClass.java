import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Core;

public class MainClass {


	public static void main(String[] args) throws IOException {
		
		int min = 100;
		int max = 700;
		int bins = 15;
		boolean print = false;
		String[] histValues;
		ArrayList<Double> vector = new ArrayList<>();

		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		/*
		 * uncomment to enable preprocessing of source folder
		PreprocessImage ppi = new PreprocessImage("images");
		ppi.convert();
		*/
		
		//uncomment to create the file containing edge count and category of all images
		createHistFile("images", min, max, bins, print);
		
		//create feature vector for test image:
		EdgeHistogram eh = new EdgeHistogram("test.png", 5, 1000, 10, 1);
		eh.calcHistogram();
		histValues = eh.evaluate(min, max, bins, print).split(",");
		for(int i = 0; i < histValues.length; i++){
			vector.add(Double.parseDouble(histValues[i]));
		}
		
		//execute KNN:
		KNearestNeighbour a = new KNearestNeighbour("histograms_100-700_15.txt");
		System.out.println(a.getKnnCategory(new FeatureVector(vector,"0"),3));

	}
	
	
	
	private static void createHistFile(String inputImagePath, int min, int max, int numOfBins, boolean print) throws IOException{
		
		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		EdgeHistogram eh;
		BufferedWriter output = new BufferedWriter(new FileWriter("histograms_" + min + "-" + max + "_" + numOfBins + ".txt"));
		int cat;
		
		cat = 1;
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
					eh = new EdgeHistogram(inputImagePath + "\\" + currFolder + "\\" + currImage, 5, 1000, 10, 1);
					eh.calcHistogram();
					output.append((eh.evaluate(min, max, numOfBins, print)));
					output.append("," + cat);
					output.append("\n");
				}
				cat++;
			}
		}
		output.flush();
		output.close();
	}
}
