import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class KNearestNeighbour {
	
	private LinkedList<FeatureVector> neighbours = new LinkedList<>();
	
	public KNearestNeighbour(){
		
		//TODO
		
	}
	
	public String getKnnCategory(FeatureVector sample, int k){
		
		LinkedList<FeatureVector> results = new LinkedList<>();
		HashMap<String, Integer> catStore = new HashMap<>();
		
		for(FeatureVector currVec: neighbours){
			if(results.size() < k){
				results.add(currVec);
			}else{
				for(FeatureVector resVec: results){
					if(currVec.getDifference(sample) > resVec.getDifference(sample)){
						results.remove(resVec);
						results.add(currVec);
					}
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
