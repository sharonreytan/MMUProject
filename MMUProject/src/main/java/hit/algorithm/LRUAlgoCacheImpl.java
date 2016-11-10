package hit.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * this algorithm uses the logic of least recently used. Therefore, each called page will be out last.
 * @author Sharon
 *
 * @param <K>
 * @param <V>
 */
public class LRUAlgoCacheImpl<K,V> implements IAlgoCache<K,V>{
	private Map <K,V> pages;
	private ArrayList<K> recentlyUsed;
	private int initialCapacity;
	
	public LRUAlgoCacheImpl(int initialCapacity){
		pages=new HashMap<K,V>();
		recentlyUsed=new ArrayList<K>();
		this.initialCapacity=initialCapacity;
	}
	@Override
	public V getElement (K key){
		if(recentlyUsed.contains(key))
		{
			int index=recentlyUsed.indexOf(key);
			recentlyUsed.remove(index);
			recentlyUsed.add(0, key); //this key will be last returned from putElement, in case of full capacity
			return pages.get(key);
		}
		return null; //if key does not exist in the stored array
	}
	@Override
	public V putElement (K key, V value){
		V curValue=null;
		if (recentlyUsed.size()==initialCapacity){
			curValue=pages.get(recentlyUsed.get(recentlyUsed.size()-1)); // the last page in the list needs to be returned and remove
			removeElement(recentlyUsed.get(recentlyUsed.size()-1));
		}
		recentlyUsed.add(0, key);
		pages.put(key,value);
		return curValue;
	}
	@Override
	public void removeElement(K key){
		if(recentlyUsed.contains(key)){
			int index=recentlyUsed.indexOf(key);
			recentlyUsed.remove(index);
			pages.remove(key);
		}
	}
	@Override
	public String toString(){
		return recentlyUsed.toString();
	}
}
