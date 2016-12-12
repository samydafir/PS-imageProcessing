import java.util.ArrayList;

public class FeatureVector {
	
	private ArrayList<Double> vector = new ArrayList<>();
	private String category;
	
	
	public FeatureVector(ArrayList<Double> features, String category){
		this.vector = features;
		this.category = category;
	}
	
	
	/**
	 * computes euklidean difference between two vectors
	 */
	public double getDifference(FeatureVector neighbour){
		
		double difference = 0;
		for(int i = 0; i < vector.size(); i++){
			difference += Math.pow(vector.get(i) - neighbour.getVector().get(i), 2);
		}
		difference = Math.sqrt(difference);
		
		return difference;
	}
	
	public ArrayList<Double> getVector(){
		return this.vector;
	}
	
	public String getCategory(){
		return this.category;
	}
	
}
