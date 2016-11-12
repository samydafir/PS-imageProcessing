import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class EdgeDetection {
	

	public static void main(String[] args) throws IOException {
		
		Mat src, edge;

		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		File input = new File("/home/sebastian/Downloads/sky.jpg");
		BufferedImage image;
			
		image = ImageIO.read(input);		
		src = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
		edge = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
		
		
		src = Imgcodecs.imread("/home/sebastian/Downloads/sky.jpg");
		edge.create( src.size(), src.type() );
		
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Imgproc.Canny(src, edge, 50, 50/3);

		Imgcodecs.imwrite("/home/sebastian/Downloads/sky2.jpg", edge);
	}

}
