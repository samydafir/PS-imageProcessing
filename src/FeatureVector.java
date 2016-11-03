import java.util.Vector;

public class FeatureVector {
	
	private Vector<Double> vector = new Vector<>();
	private String category;
	
	
	public FeatureVector(Vector<Double> features, String category){
		this.vector = features;
		this.category = category;
	}
	
	
	/**
	 * computes euklidean difference between two vectors
	 */
	public double getDifference(FeatureVector neighbour){
		
		double difference = 0;
		for(int i = 0; i < vector.size(); i++){
			difference += Math.pow(vector.get(i) - neighbour.getVector().elementAt(i), 2);
		}
		difference = Math.sqrt(difference);
		
		return difference;
	}
	
	public Vector<Double> getVector(){
		return this.vector;
	}
	
	public String getCategory(){
		return this.category;
	}
	
}
