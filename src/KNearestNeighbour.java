import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class KNearestNeighbour {
	
	private LinkedList<FeatureVector> neighbours = new LinkedList<>();
	
	public KNearestNeighbour(String histogramFile) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(histogramFile));
		
		String currLine = input.readLine();
		ArrayList<Double> vector;
		String[] histValues;

		while(currLine != null){
			histValues = currLine.split(",");
			vector = new ArrayList<>();
			for(int i = 0; i < histValues.length - 1; i++){
				vector.add(Double.parseDouble(histValues[i]));
			}
			neighbours.add(new FeatureVector(vector, histValues[histValues.length - 1]));
			currLine = input.readLine();
		}
		input.close();
	}
	
	public String getKnnCategory(FeatureVector sample, int k){
		
		LinkedList<FeatureVector> results = new LinkedList<>();
		HashMap<String, Integer> catStore = new HashMap<>();
		FeatureVector currMax = null;
		
		for(FeatureVector currVec: neighbours){			
			if(results.size() < k){
				results.add(currVec);
			}else{
				currMax = results.getFirst();
				for(FeatureVector resVec: results){
					if(resVec.getDifference(sample) > currMax.getDifference(sample)){
						currMax = resVec;
					}
				}
				if(currVec.getDifference(sample) < currMax.getDifference(sample)){
					results.remove(currMax);
					results.add(currVec);
				}
			}
		}
		
		String currCat;
		for(FeatureVector FVec: results){
			currCat = FVec.getCategory();
			if(catStore.containsKey(currCat)){
				catStore.put(currCat, catStore.get(currCat) + 1);
			}else{
				catStore.put(FVec.getCategory(), 1);
			}
		}
		
		int max = 0;
		String resultCat = "no result";
		for(Map.Entry<String, Integer> currRes: catStore.entrySet()){
			if(currRes.getValue() > max){
				max = currRes.getValue();
				resultCat = currRes.getKey();
			}
		}		
		
		return resultCat;
	}	
}
