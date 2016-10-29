import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ColorModelConverter {

	public static void main(String[] args) throws IOException {
		
		File file = new File(args[0]);
		int width = Integer.parseInt(args[1]);
		int height = Integer.parseInt(args[2]);
		BufferedImage img = ImageIO.read(file);
		
		
		int[][][] pixelRgbValues = new int[height][width][3];
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				pixelRgbValues[i][j] = getPixelRgbValues(new Color(img.getRGB(j, i)));
			}
		
		}
		
	}

	public static int[] getPixelRgbValues(Color color){
		int[] rgb = new int[3];
		rgb[0] = color.getRed();
		rgb[1] = color.getGreen();
		rgb[2] = color.getBlue();	
		
		return rgb;
		
	}
	
	
	
	public static double[][][] rgbToYuv(int[][][] rgbValues, int width, int height){
		
		double[][][] yuvValues = new double[height][width][3];
		int red, green, blue;
		double y, u, v;
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				red = rgbValues[i][j][0];
				green = rgbValues[i][j][1];
				blue = rgbValues[i][j][2];
				y = 0.299 * red + 0.687 * green + 0.114 * blue;
				u = ((double)blue - y) * 0.493;
				v = ((double)red - y) * 0.493;
				yuvValues[i][j][0] = y;
				yuvValues[i][j][1] = u;
				yuvValues[i][j][2] = v;
				
			}
		}
		
		return yuvValues;
	}
	
	
	public static float[][][] rgbToHsb(int[][][] rgbVals, int height, int width){
		
		float[][][] hsbVals = new float[height][width][3];
		int red, green, blue;
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				red = rgbVals[i][j][0];
				green = rgbVals[i][j][1];
				blue = rgbVals[i][j][2];
				hsbVals[i][j] = Color.RGBtoHSB(red, green, blue, null);
			}
		}
		
		
		return hsbVals;
	}
}














