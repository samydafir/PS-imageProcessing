import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public class PreprocessImage {

	public static void main( String[] args ) {
	
		String[] colorModes = {"lab", "yuv", "hsl", "hsv"};
		HashMap<String, BufferedImage> images = new HashMap<>();
		String inputImagePath = args[0];
		
		try{
			for (String currMode: colorModes){
				images.put(currMode, colorSpaceConvert(inputImagePath, currMode));
			}
		} catch (IOException e){}   
	
	}





   public static BufferedImage colorSpaceConvert(String imagePath, String colorMode) throws IOException{
	   
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
	   default:
		   colorSpace = 0;
	   }
	   
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
       File input = new File(imagePath);
       BufferedImage image = ImageIO.read(input);	
       byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
       
       Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);

       Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
       Imgproc.cvtColor(mat, mat1, colorSpace);

       byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
       mat1.get(0, 0, data1);
       BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
       image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

       File ouptut = new File(colorMode + ".jpg");
       ImageIO.write(image1, "jpg", ouptut);
       
       return image1;
   }
   
   public static BufferedImage clahe(BufferedImage image){
	   
	   byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
       
       Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);
            
       //TODO
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   return null;
   }
       
}





