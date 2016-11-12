import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public class PreprocessImage {

	public static void main( String[] args ) throws IOException {
	
		String[] colorModes = {"lab", "yuv", "hsl", "hsv"};
		HashMap<String, BufferedImage> images = new HashMap<>();
		String inputImagePath = "E:\\OneDrive - stud.sbg.ac.at\\University\\WS16\\Grundlagen Bildverarbeitung\\PS-imageProcessing\\example.jpg";
		
		try{
			images.put("rgb", ImageIO.read(new File(inputImagePath)));/*
			for (String currMode: colorModes){
				images.put(currMode, colorSpaceConvert(ImageIO.read(new File(inputImagePath)), currMode));
				
			}*/
			normalize(images.get("rgb"));
		} catch (IOException e){
			e.printStackTrace();
		}	
	}





   public static BufferedImage colorSpaceConvert(BufferedImage image, String colorMode) throws IOException{
	   
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
	   
       byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
       
       Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);

       Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
       Imgproc.cvtColor(mat, mat1, colorSpace);

       byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
       mat1.get(0, 0, data1);
       BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
       image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

       File ouptut = new File("output\\" + colorMode + ".jpg");
       ImageIO.write(image1, "jpg", ouptut);
       
       return image1;
   }
   
   
   public static BufferedImage clahe(BufferedImage image) throws IOException{
	   
	   BufferedImage lab = colorSpaceConvert(image, "lab");
	   
	   byte[] data = ((DataBufferByte) lab.getRaster().getDataBuffer()).getData();
       
       Mat mat = new Mat(lab.getHeight(),lab.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);
              
       Mat luminance = new Mat();
       //Core.extractChannel(mat, luminance, 0);;
       
       CLAHE clahe = Imgproc.createCLAHE();
       clahe.setClipLimit(2);
       clahe.setTilesGridSize(new Size(8,8));
       //clahe.apply(channels.get(0), channels.get(0));

       //Mat mat1 = new Mat(lab.getHeight(), lab.getWidth(), CvType.CV_8UC3);

       //Core.insertChannel(luminance, mat, 0);
       
       byte[] data1 = new byte[mat.rows()*mat.cols()*(int)(mat.elemSize())];
       mat.get(0, 0, data1);
       BufferedImage image1 = new BufferedImage(mat.cols(), mat.rows(), 5);
       image1 = colorSpaceConvert(image, "rgb");
       image1.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data1);

       File ouptut = new File("output\\clahe.jpg");
       ImageIO.write(image1, "jpg", ouptut);
            	  
	   return image1;
   }
   
   
   
   public static BufferedImage normalize(BufferedImage image) throws IOException{

	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	   
	   byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

	   data = normalize(data, image.getHeight(), image.getWidth());
	   Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);
       BufferedImage image1 = new BufferedImage(mat.cols(),mat.rows(), 5);

       image1.getRaster().setDataElements(0, 0,mat.cols(),mat.rows(), data);

       File output = new File("output\\normalized.jpg");
       ImageIO.write(image1, "jpg", output);
       
       
	   /*
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

	   byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
       
       Mat mat = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
       mat.put(0, 0, data);
	   
       Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
       
       Core.normalize(mat, mat1);;
      
        
       byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
       BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), 5);
       image1.getRaster().setDataElements(0, 0,mat1.cols(),mat1.rows(), data1);

       File output = new File("output\\normalized.jpg");
       ImageIO.write(image1, "jpg", output);
       */

	   return null;
   }
   
   
   public static byte[] normalize(byte[] rgbVals, int height, int width){
		
		int pixels = height * width;
		int[] rgbMean = {0,0,0};
		for(int i = 0; i < rgbVals.length; i++){
			if(i < 100)
				System.out.print(rgbVals[i] + " ");
			
			if(i % 3 == 0)
				rgbMean[0] += rgbVals[i];
			else if(i % 3 == 1)
				rgbMean[1] += rgbVals[i];
			else
				rgbMean[2] += rgbVals[i];
		}
		System.out.println();
		rgbMean[0] /= pixels;
		rgbMean[1] /= pixels;
		rgbMean[2] /= pixels;
		
		for(int a: rgbMean)
			System.out.println(a);
			
		byte[] normRGB = new byte[rgbVals.length];
		for(int i = 0; i < rgbVals.length; i++){
			if(i % 3 == 0)
				normRGB[i] = (byte)(rgbVals[i] - rgbMean[0]);
			else if(i % 3 == 1)
				normRGB[i] = (byte)(rgbVals[i] - rgbMean[1]);
			else
				normRGB[i] = (byte)(rgbVals[i] - rgbMean[2]);
			
			if(i < 100)
				System.out.print(normRGB[i] + " ");
		}
		
		return normRGB;
	}

       
}





