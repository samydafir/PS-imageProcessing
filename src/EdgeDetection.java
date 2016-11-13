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
		
		Mat src, sobX, sobY, lines, edge, im;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				
		src = new Mat();
		sobX = new Mat();
		sobY = new Mat();
		edge = new Mat();
		lines = new Mat();
		im = new Mat();
		
		
		src = Imgcodecs.imread(srcPath);
		edge.create( src.size(), src.type() );
		lines.create( src.size(), src.type() );
		im.create( src.size(), src.type() );
		
		
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Imgproc.Sobel(src, sobX, src.depth(), 1, 0);
		Imgproc.Sobel(src, sobY, src.depth(), 0, 1);
		Imgproc.Canny(src, edge, lowThresh, highThresh);
		Imgproc.HoughLines(edge, lines, 1, Math.PI/180, 10);
		
		
		for (int i = 0; i < lines.cols(); i++){
			double data[] = lines.get(0, i);
			double rho = data[0];
			double theta = data[1];
			double cosTheta = Math.cos(theta);
			double sinTheta = Math.sin(theta);
			double x0 = cosTheta * rho;
			double y0 = sinTheta * rho;
			Point pt1 = new Point(x0 + 10000 * (-sinTheta), y0 + 10000 * cosTheta);
			Point pt2 = new Point(x0 - 10000 * (-sinTheta), y0 - 10000 * cosTheta);
			Imgproc.line(im, pt1, pt2, new Scalar(0, 0, 200), 3);
}
		

		Imgcodecs.imwrite(dstPath + "_canny.jpg", edge);
		Imgcodecs.imwrite(dstPath + "_sobelX.jpg", sobX);
		Imgcodecs.imwrite(dstPath + "_sobelY.jpg", sobY);
		Imgcodecs.imwrite(dstPath + "_houghLines1.jpg", im);
		
		
	}

}
