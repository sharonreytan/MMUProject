package hit.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * this algorithm's logic, is that the least frequently used page will be out when in full capacity.
 * the map timesUsed counts the times each page has been in use, thorugh getElement and putElement.
 * @author Sharon
 *
 * @param <K>
 * @param <V>
 */
public class LFUAlgoCacheImpl<K,V> implements IAlgoCache<K,V>{
	private java.util.Map <K,V> pages;
	private HashMap<K,Integer> timesUsed;
	private int initialCapacity;

	public LFUAlgoCacheImpl(int initialCapacity){
		pages=new HashMap<K,V>();
		timesUsed=new HashMap<K,Integer>();
		this.initialCapacity=initialCapacity;
	}
	@Override
	public V getElement (K key){
		if(timesUsed.containsKey(key))
		{
			Integer timesUsedIndex;
			timesUsedIndex=timesUsed.get(key);
			timesUsed.remove(key);
			timesUsed.put(key, timesUsedIndex+1);
			return pages.get(key);
		}
		return null; //if key does not exist in the stored array
	}
	@Override
	public V putElement (K key, V value){
		V curValue=null;
		if (timesUsed.size()==initialCapacity){
			K minInt = findMin(); //the least frequetly used page is the minimum from the map
			curValue = pages.get(minInt);
			removeElement(minInt);
		}
		timesUsed.put(key, 1);
		pages.put(key,value);
		return curValue;
	}
	@Override
	public void removeElement(K key){
		timesUsed.remove(key);
		pages.remove(key);
	}
	@Override
	public String toString(){
		return timesUsed.toString();
	}
	
	private K findMin() {
		Integer minValue=(Collections.min(timesUsed.values()));  
        for (Entry<K, Integer> entry : timesUsed.entrySet()) { 
            if (entry.getValue()==minValue) {                
                return entry.getKey();
            }
        }
        return null;
	}
	
}