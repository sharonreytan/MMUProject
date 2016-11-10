package hit.memoryunits;

import java.util.HashMap;
import java.util.Map;

public class RAM {
	private int initialCapacity;
	private Map<Long,Page<byte[]>>	pages;
	
	public RAM(int initialCapacity){
		this.initialCapacity=initialCapacity;
		pages=new HashMap<Long,Page<byte[]>>();
	}
	public void addPage (Page<byte[]> addPage) {
		if (!(pages.containsKey(addPage.getPageId())))
				this.pages.put(addPage.getPageId(), addPage);
	}
	public void addPages (Page<byte[]>[] addPages) {
		for (int i=0; i<addPages.length; i++)
				this.addPage(addPages[i]);
	}
	boolean isFull(){
		if (pages.size()==initialCapacity)
			return true;
		else return false;
	}
	public int getInitialCapacity() {
		return initialCapacity;
	}
	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}
	public java.util.Map<Long,Page<byte[]>> getPages() {
		return pages;
	}
	public void setPages(java.util.Map<Long,Page<byte[]>> pages) {
		this.pages = pages;
	}
	public void removePage(Page<byte[]> removePage) {
		pages.remove(removePage.getPageId());
	}
	public void removePages(Page<byte[]>[] removePages) {
		for (int i=0; i<pages.size(); i++)
			this.removePage(removePages[i]);
	}
	public Page<byte[]> getPage(Long pageId) {
			return pages.get(pageId);
		}
	public String toString() {
		return "initialCapacity: " + initialCapacity + ", current size: " + pages.size() + ", pages: " + pages ;
	}
}
