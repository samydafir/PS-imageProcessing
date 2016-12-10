
import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

/**
 * Call System.loadLibrary( Core.NATIVE_LIBRARY_NAME ) before using this class.
 */
public class PreprocessImage {
	
	private int count;
	private String inputImagePath;
	
	public PreprocessImage(String inputImagePath){
		this.inputImagePath = inputImagePath;
		count = 0;
	}
	
	public void convert() throws IOException{
	
		String[] colorModes = {"lab", "yuv", "hsl", "hsv"};
		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		File outputFolder;
		
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currMode: colorModes){
					outputFolder = new File("output\\" + currFolder + "\\" + currMode + "\\");
					if(!outputFolder.exists()){
						outputFolder.mkdirs();
					}
					count = 0;
					for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
						normalize(inputImagePath + "\\" + currFolder + "\\" + currImage);
						clahe("output\\normalized.jpg");
						colorSpaceConvert("output\\clahe.jpg", currMode,  "output\\" + currFolder + "\\" + currMode + "\\" + count + ".jpg");
						count++;
					}
				}
			}
		}
	}




   private Mat colorSpaceConvert(String path, String colorMode, String output) throws IOException{
	   
	   int colorSpace;
	   
	   switch(colorMode){
	   case "hsv":
		   colorSpace = Imgproc.COLOR_RGB2HSV;
		   break;
	   case "hsl":
		   colorSpace = Imgproc.COLOR_RGB2HLS;
		   break;
	   case "yuv":
		   colorSpace = Imgproc.COLOR_RGB2YUV;
		   break;
	   case "lab":
		   colorSpace = Imgproc.COLOR_RGB2Lab;
		   break;
	   case "rgb":
		   colorSpace = Imgproc.COLOR_Lab2RGB;
		   break;
	   default:
		   colorSpace = 0;
	   }
	   	   
	   Mat src = Imgcodecs.imread(path);

       Imgproc.cvtColor(src, src, colorSpace);
       
       Imgcodecs.imwrite(output, src);       
       
       return src;
   }
   
   
   private Mat clahe(String path) throws IOException{
	          
	   Mat src = Imgcodecs.imread(path);

	   for(int i = 0; i < 3; i++){
		   Mat channel = new Mat();
		   Core.extractChannel(src, channel, i);;
       
		   CLAHE clahe = Imgproc.createCLAHE();
		   clahe.setClipLimit(1);
		   clahe.setTilesGridSize(new Size(4,4));
		   clahe.apply(channel, channel);
		   
		   Core.insertChannel(channel, src, i);
	   }
		   
       Imgcodecs.imwrite("output\\clahe.jpg", src);
       
       return src;
   }
   
   
   
   private void normalize(String path) throws IOException{

	   Mat src = Imgcodecs.imread(path);
       Core.normalize(src, src, 255, 0, Core.NORM_MINMAX);
       
	  
       Imgcodecs.imwrite("output\\normalized.jpg", src);
       
   }       
}