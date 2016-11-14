import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class EdgeDetection {
	

	public static void main(String[] args) throws IOException {
		
		detect("examples/edge/src1.jpg", "/home/sebastian/Documents/Uni/imgproc/dst1", 60, 60/3);
		detect("examples/edge/src2.jpg", "/home/sebastian/Documents/Uni/imgproc/dst2", 100, 100/3);
		
	}
	
	
	public static void detect(String srcPath, String dstPath, int highThresh, int lowThresh) throws IOException {
		
		Mat src, sobX, sobY, edge;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				
		src = new Mat();
		sobX = new Mat();
		sobY = new Mat();
		edge = new Mat();
		
		
		src = Imgcodecs.imread(srcPath);
		edge.create( src.size(), src.type() );
		
		
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Imgproc.Sobel(src, sobX, src.depth(), 1, 0);
		Imgproc.Sobel(src, sobY, src.depth(), 0, 1);
		Imgproc.Canny(src, edge, lowThresh, highThresh);		
		

		Imgcodecs.imwrite(dstPath + "_canny.jpg", edge);
		Imgcodecs.imwrite(dstPath + "_sobelX.jpg", sobX);
		Imgcodecs.imwrite(dstPath + "_sobelY.jpg", sobY);
		
		
	}

}
