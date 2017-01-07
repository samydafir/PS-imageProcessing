import java.util.HashSet;
import java.util.LinkedList;
import org.opencv.core.Mat;


public class EdgeOrientationExtractor {


		private Mat imageWithEdges;
		private LinkedList<Double> orientation = new LinkedList<>();		
		
		public EdgeOrientationExtractor(Mat input) {
			imageWithEdges = input;
		}
		
		public void computeEdgeOrientation(){
			for(int i = 0; i < imageWithEdges.height(); i++){
				for(int j = 0; j < imageWithEdges.width(); j++){					
					orientation.add(imageWithEdges.get(i,j)[0]);
				}
			}
		}
		
		public LinkedList<Double> getEdgeOrientation(){
			return orientation;
		}
	}
