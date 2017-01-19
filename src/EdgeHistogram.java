import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


/*
 * NOTE: don't forget to load the opencv library before using this class:  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
 */

public class EdgeHistogram {

	private Mat srcMat;		// source Mat
	private Mat cannyMat;	// Mat that contains the edge detected image
	private Mat sobelMatX;	// Mat that contains first derivation in x of image
	private Mat sobelMatY;	// Mat that contains first derivation in y of image
	private Mat greyMat; 	// Mat containing grey image of src image
	private Mat edgeDirMat;	// Mat that contains the orientation of the edges
	private Mat histMat;	// final Mat that contains the histogram

	private int highThresh;	// high threshold for canny edge detection
	private int lowThresh;	// low threshhold

	private List<Mat> histMatList;
	private LinkedList<Integer> edgeLengths; //List containing lengths of all found edges
	private LinkedList<Double> edgeOrientation; //List containing orientation


	public EdgeHistogram(String srcPath, int maxEdgeLength, int highThresh, int lowThresh) {

		this.highThresh = highThresh;
		this.lowThresh = lowThresh;

		this.srcMat = new Mat();
		this.cannyMat = new Mat();
		this.sobelMatX  = new Mat();
		this.sobelMatY = new Mat();
		this.greyMat = new Mat();
		this.edgeDirMat = new Mat();

		this.srcMat = Imgcodecs.imread(srcPath);
		cannyMat.create(srcMat.size(), srcMat.type());

		histMatList = new ArrayList<>();
	}

	private void convertToGrey(){
		Imgproc.cvtColor(srcMat, greyMat, Imgproc.COLOR_RGB2GRAY);
	}

	private void extractEdges() {
		Imgproc.blur(greyMat, cannyMat, new Size(3, 3));
		Imgproc.Canny(cannyMat, cannyMat, lowThresh, highThresh);
		histMatList.add(cannyMat);
	}

	private void extractFirstDerivative(){
		Imgproc.Sobel(greyMat, sobelMatX, srcMat.depth(), 1, 0);
		Imgproc.Sobel(greyMat, sobelMatY, srcMat.depth(), 0, 1);
		Core.convertScaleAbs(sobelMatX, sobelMatX);
		Core.convertScaleAbs(sobelMatY, sobelMatY);
	}

	private void getEdgeLengths() {
		EdgeLengthExtractor elx = new EdgeLengthExtractor(cannyMat);
		elx.computeEdgeLenghts();
		edgeLengths = elx.getEdgeLengths();
	}

	private void getEdgeOrientation(){
		Core.addWeighted( sobelMatX, 0.5, sobelMatY, 0.5, 0, edgeDirMat);
		EdgeOrientationExtractor eox = new EdgeOrientationExtractor(edgeDirMat);
		eox.computeEdgeOrientation();
		edgeOrientation = eox.getEdgeOrientation();
	}

	public void calcHistogram() {
		convertToGrey();
		extractEdges();
		extractFirstDerivative();
		getEdgeLengths();
		getEdgeOrientation();
	}

	public Mat getHistogram() {
		return this.histMat;
	}

	public String evaluatelength(int minThreshold, int maxThreshold, int numOfBins, boolean print){
		int min = 0;
		int max = 0;
		if (edgeLengths.size() > 0) {
			min = edgeLengths.getFirst();
			max = edgeLengths.getFirst();
		}
		int diff, rangePerBin;
		int[] amounts = new int[numOfBins];
		String result;

		diff = maxThreshold - minThreshold;
		rangePerBin = (int)Math.round((double)diff / (double)numOfBins);

		for(int currLength: edgeLengths){

			if(currLength > max){
				max = currLength;
			}else if(currLength < min){
				min = currLength;
			}
			if(currLength >= minThreshold && currLength <= maxThreshold)
			amounts[((int)Math.ceil((double)currLength / (double)maxThreshold * numOfBins)) - 1] ++;
		}

		StringBuilder sb = new StringBuilder();

		if(print){
			System.out.println("minimum: " + min);
			System.out.println("maximum: " + max);
			System.out.println("rangePerBin: " + rangePerBin);
			System.out.println("bin values:");
		}
		for(int i = 0; i < amounts.length; i++){
			sb.append(amounts[i] + ".0,");
			if(print){
				System.out.print(amounts[i] + ",");
				System.out.println("range: " + (minThreshold + i * rangePerBin) + "-" + (minThreshold + (i + 1) * rangePerBin));
			}
		}
		result = sb.toString();
		return result.substring(0, result.length() - 1);
	}

	public String evaluateOrientation(int numOfBins, boolean print){
		double maxDegree = 180.0;
		double minAngle = edgeOrientation.getFirst();
		double maxAngle = edgeOrientation.getFirst();
		int rangePerBin;
		int[] amounts = new int[numOfBins];
		String result;

		rangePerBin = (int)((maxDegree) / numOfBins);

		for(double currAngle: edgeOrientation){
			if(currAngle > maxAngle){
				maxAngle = currAngle;
			}else if(currAngle < minAngle){
				minAngle = currAngle;
			}
			amounts[((int)Math.ceil((double)currAngle / numOfBins))] ++;
		}

		StringBuilder sb = new StringBuilder();

		if(print){
			System.out.println("minimum: " + minAngle);
			System.out.println("maximum: " + maxAngle);
			System.out.println("rangePerBin: " + rangePerBin);
			System.out.println("bin values:");
		}
		for(int i = 0; i < amounts.length; i++){
			sb.append((int)(((double)amounts[i])/100) + ".0,");
			if(print){
				System.out.print(amounts[i] + " |");
				System.out.println("range: " + i * rangePerBin + "-" + (i + 1) * rangePerBin);
			}
		}
		result = sb.toString();
		return result.substring(0, result.length() - 1);
	}
}