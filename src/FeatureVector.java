import java.util.ArrayList;

public class FeatureVector {
	
	private ArrayList<Double> vector = new ArrayList<>();
	private int category;
	private int patient;
	
	
	public FeatureVector(ArrayList<Double> features, int category, int patient){
		this.vector = features;
		this.category = category;
		this.patient = patient;
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
	
	public int getCategory(){
		return this.category;
	}
	
	public int getPatient() {
		return patient;
	}


	public void setPatient(int patient) {
		this.patient = patient;
	}

//	public boolean equals(FeatureVector other){
//		if(other.getVector().equals(this.vector) && other.getCategory().equals(this.category)){
//			return true;
//		}
//		return false;
//		
//	}
	
}
