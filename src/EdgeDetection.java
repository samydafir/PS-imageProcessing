import java.io.IOException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class EdgeDetection {
	

	public static void main(String[] args) throws IOException {
		
		int thresh2 = 100;
		

		canny("examples/edge/src3.jpg", "dst/dst", thresh2, thresh2/3);
		
	}
	
	
	public static void canny(String srcPath, String dstPath, int highThresh, int lowThresh) throws IOException {
		
		Mat src, sobX, sobY, edge, laplace, ori;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				
		src = new Mat();
		sobX = new Mat();
		sobY = new Mat();
		edge = new Mat();
		ori = new Mat();
		laplace = new Mat();
		
		src = Imgcodecs.imread(srcPath);
		edge.create(src.size(), src.type());
		Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Imgproc.Sobel(src, sobX, src.depth(), 1, 0);
		Imgproc.Sobel(src, sobY, src.depth(), 0, 1);
		Core.convertScaleAbs(sobX, sobX);
		Core.convertScaleAbs(sobY, sobY);
		Core.addWeighted( sobX, 0.5, sobY, 0.5, 0, ori);
		for(int i = 0; i < ori.height(); i++){
			for(int j = 0; j < ori.width(); j++){
				System.out.print(ori.get(i, j)[0]);
				System.out.println();
			}
		}
		
		Imgproc.Canny(src, edge, lowThresh, highThresh);	
		Imgproc.Laplacian(src, laplace, src.depth(), 3, 1, 0);
		

		Imgcodecs.imwrite(dstPath + "_canny.jpg", edge);
		Imgcodecs.imwrite(dstPath + "_sobelX.jpg", sobX);
		Imgcodecs.imwrite(dstPath + "_sobelY.jpg", sobY);
		Imgcodecs.imwrite(dstPath + "_laplacian.jpg", laplace);
		
		
	}

}
