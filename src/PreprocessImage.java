
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public class PreprocessImage {

	public static void main( String[] args ) throws IOException {
	
		String[] colorModes = {"lab", "yuv", "hsl", "hsv"};
		String inputImagePath = "example.jpg";
		
		try{
			for (String currMode: colorModes){
				colorSpaceConvert(inputImagePath, currMode);
				
			}
			normalize(inputImagePath);
			clahe(inputImagePath);
		} catch (IOException e){
			e.printStackTrace();
		}	
	}





   public static Mat colorSpaceConvert(String path, String colorMode) throws IOException{
	   
	   int colorSpace;
	   
	   switch(colorMode){
	   case "hsv":
		   colorSpace = Imgproc.COLOR_BGR2HSV;
		   break;
	   case "hsl":
		   colorSpace = Imgproc.COLOR_BGR2HLS;
		   break;
	   case "yuv":
		   colorSpace = Imgproc.COLOR_BGR2YUV;
		   break;
	   case "lab":
		   colorSpace = Imgproc.COLOR_BGR2Lab;
		   break;
	   case "rgb":
		   colorSpace = Imgproc.COLOR_Lab2BGR;
		   break;
	   default:
		   colorSpace = 0;
	   }
	   
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	   
	   Mat src = Imgcodecs.imread(path);

       Imgproc.cvtColor(src, src, colorSpace);
       
       Imgcodecs.imwrite("output\\" + colorMode + ".jpg", src);       
       
       return src;
   }
   
   
   public static Mat clahe(String path) throws IOException{
	          
	   Mat src = Imgcodecs.imread("output\\normalized.jpg");

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
   
   
   
   public static void normalize(String path) throws IOException{

	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

	   Mat src = Imgcodecs.imread(path);
       Core.normalize(src, src, 255, 0, Core.NORM_MINMAX);
       
	  
       Imgcodecs.imwrite("output\\normalized.jpg", src);
       
   }       
}