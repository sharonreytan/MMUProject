package hit.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MFUAlgoCacheImpl<K, V> implements IAlgoCache<K, V>{

	//Internal data structure which holds the real data
	private Map<K, V> cache;
	
	//Internal data structure which needs to perform the logic of this algorithm
	private Map<K, Integer> cacheCounter;
	
	private int capacity;

	public MFUAlgoCacheImpl(int capacity){
		cache = new HashMap<>(capacity);
		cacheCounter = new HashMap<>();
		this.capacity = capacity;
	}


	@Override
	public V getElement(K key) {
		if(cache.containsKey(key)){
			Integer count = cacheCounter.get(key);
			cacheCounter.put(key, ++count);
			return cache.get(key);
		}
		return null;
	}

	@Override
	public V putElement(K key, V value) {
		V curValue = null;
		if(capacity == cache.size()){
			K maxKey = findMax();
			curValue = getElement(maxKey);
			removeElement(maxKey);
		}
		cache.put(key, value);
		cacheCounter.put(key, 0);
		return curValue;
	}

	@Override
	public void removeElement(K key) {
		if(cache.containsKey(key)){
			cache.remove(key);
			cacheCounter.remove(key);
		}
	}

	private K findMax() {
		Integer maxValueInMap=(Collections.max(cacheCounter.values()));  // This will return max value in the Hashmap
        for (Entry<K, Integer> entry : cacheCounter.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==maxValueInMap) {                
                return entry.getKey();// return the key with max value
            }
        }
        return null;
	}

	public String toString(){
		return cache.toString();
	}
}
