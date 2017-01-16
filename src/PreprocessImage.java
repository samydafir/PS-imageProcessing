
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
	
	private String inputImagePath;
	private Mat image;
	
	public PreprocessImage(String inputImagePath){
		this.inputImagePath = inputImagePath;
	}
	
	public void convert(String[] colorModes) throws IOException{
	
		
		String[] folders = new File(inputImagePath).list();
		File currentFolder;
		File outputFolder;
		int count = 0;
		
		for(String currFolder: folders){
			currentFolder = new File(inputImagePath + "\\" + currFolder);
			if(currentFolder.isDirectory()){
				for(String currMode: colorModes){
					outputFolder = new File(currMode + "\\" + count);
					if(!outputFolder.exists()){
						outputFolder.mkdirs();
					}
					for(String currImage: new File(inputImagePath + "\\" + currFolder).list()){
						image = Imgcodecs.imread(inputImagePath + "\\" + currFolder + "\\" + currImage);
						normalize();
						clahe();
						colorSpaceConvert(currMode);
						Imgcodecs.imwrite(outputFolder +  "\\" + currImage, image);
					}
				}
			}
		count++;
		}
	}




   private void colorSpaceConvert(String colorMode) throws IOException{
	   
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
	   default:
		   colorSpace = 0;
	   }
	   

	   if(colorSpace != 0){
		   Imgproc.cvtColor(image, image, colorSpace);
	   }
       
   }
   
   
   private void clahe() throws IOException{
	          

	   for(int i = 0; i < 3; i++){
		   Mat channel = new Mat();
		   Core.extractChannel(image, channel, i);;
       
		   CLAHE clahe = Imgproc.createCLAHE();
		   clahe.setClipLimit(1);
		   clahe.setTilesGridSize(new Size(4,4));
		   clahe.apply(channel, channel);
		   
		   Core.insertChannel(channel, image, i);
	   }       
   }
   
   
   
   private void normalize() throws IOException{

       Core.normalize(image, image, 255, 0, Core.NORM_MINMAX);       
   }       
}