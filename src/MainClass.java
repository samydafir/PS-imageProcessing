import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Core;

public class MainClass {


	public static void main(String[] args) throws IOException {
		
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		/*
		 * uncomment to enable preprocessing of source folder
		PreprocessImage ppi = new PreprocessImage("images");
		ppi.convert();
		*/
		createHistFile("images", 100, 1000, 20, false);
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
					output.append("\t" + cat);
					output.append("\n");
				}
				cat++;
			}
		}
		output.flush();
		output.close();
	}
}
