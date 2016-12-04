import java.util.HashSet;
import java.util.LinkedList;
import org.opencv.core.Mat;

public class EdgeLengthExtractor {

	private Mat imageWithEdges;
	private LinkedList<Integer> lengths = new LinkedList<>();
	private HashSet<String> countedPixels = new HashSet<>();
	private int edgeColorLimit = 200;
	
	
	
	public EdgeLengthExtractor(Mat input) {
		imageWithEdges = input;
	}
	
	public void computeEdgeLenghts(){
		int currLength;
		
		for(int i = 0; i < imageWithEdges.height(); i++){
			for(int j = 0; j < imageWithEdges.width(); j++){
				if(imageWithEdges.get(i, j)[0] >= edgeColorLimit && !countedPixels.contains((j + "x" + i)))
					countedPixels.add(j + "x" + i);
					currLength = measureEdge(i, j);
					if(currLength > 0)
						lengths.add(currLength);
			}
		}
	}
	
	private int measureEdge(int y, int x){
		int length = 0;
		
		if(y < 0 || x  < 0) 
			return 0;
		
		if(!countedPixels.contains((y + "x" + x)) && imageWithEdges.get(y, x) != null && imageWithEdges.get(y, x)[0] >= edgeColorLimit){
			countedPixels.add(y + "x"+ x);
			length += 1;
			length += measureEdge(y - 1, x - 1);
			length += measureEdge(y - 1, x);
			length += measureEdge(y - 1, x + 1);
			length += measureEdge(y, x + 1);
			length += measureEdge(y + 1, x + 1);
			length += measureEdge(y + 1, x);
			length += measureEdge(y + 1, x - 1);
			length += measureEdge(y, x - 1);
		}
		
		return length;
	}
	
	public LinkedList<Integer> getEdgeLengths(){
		return lengths;
	}
}
