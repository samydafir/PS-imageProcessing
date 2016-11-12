import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ColorModelConverter {

	public static void main(String[] args) throws IOException {
		
		File file = new File(args[0]);
		
		BufferedImage img = ImageIO.read(file);
		int width = img.getWidth();
		int height = img.getHeight();
		
		int[][][] pixelRgbValues = new int[height][width][3];
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				pixelRgbValues[i][j] = getPixelRgbValues(new Color(img.getRGB(j, i)));
			}
		}		
		normalize(pixelRgbValues, height, width);
	}

	
	public static int[] getPixelRgbValues(Color color){
		int[] rgb = new int[3];
		rgb[0] = color.getRed();
		rgb[1] = color.getGreen();
		rgb[2] = color.getBlue();	
		
		return rgb;
		
	}
	
	
	public static double[][][] rgbToYuv(int[][][] rgbValues, int height, int width){
		
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
				System.out.println(y + " " + u + " " + v);

				
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
				Color.RGBtoHSB(red, green, blue, hsbVals[i][j]);
			}
		}
		
		
		return hsbVals;
	}
	
	
	public static int[][][] normalize(int[][][] rgbVals, int height, int width){
		
		int pixels = height * width;
		double[] rgbMean = {0,0,0};
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				rgbMean[0] += rgbVals[i][j][0];
				rgbMean[1] += rgbVals[i][j][1];
				rgbMean[2] += rgbVals[i][j][2];
			}
		}
		rgbMean[0] /= pixels;
		rgbMean[1] /= pixels;
		rgbMean[2] /= pixels;
		
		int[][][] normRGB = new int[height][width][3];
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				normRGB[i][j][0] = (int)(rgbVals[i][j][0] - rgbMean[0]);
				normRGB[i][j][1] = (int)(rgbVals[i][j][1] - rgbMean[1]);
				normRGB[i][j][0] = (int)(rgbVals[i][j][2] - rgbMean[2]);
			}
		}
		return normRGB;
	}
}