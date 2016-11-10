package hit.algorithm;
/**
 * in this interface, each algorithm has its logic so that:
 * putElement - add a page to the algorithm. If we are out of space, it returns a page.
 * getElement - updates the order of pages that putElement will return, in case of full capacity
 * removeElement - remove an element from the algorithm's order
 * 
 * each algorithm has its container, which helps it decide who will be returned from putElement, and a map
 * of the current RAM state.
 * @author Sharon
 *
 * @param <K>
 * @param <V>
 */
public interface IAlgoCache<K,V> {
	public abstract V getElement (K key);
	public abstract V putElement (K key, V value);
	public abstract void removeElement(K key);
}
