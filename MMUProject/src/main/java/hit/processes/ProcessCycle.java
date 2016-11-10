package hit.processes;

import java.util.List;

public class ProcessCycle {
	private List<byte[]> data; //updated content for each page
	private List<Long> pages; //page IDs needed
	private int sleepMs; //time of sleep
	
	ProcessCycle(List<Long> pages, int sleepMs, List<byte[]> data){
		this.pages=pages;
		this.data=data;
		this.sleepMs=sleepMs;
	}
	List<byte[]> getData() {return data;}
	List<Long> getPages() {return pages;}
	int getSleepMs() {return sleepMs;}
	void setData(List<byte[]> data) {this.data=data;}
	void setPages(List<Long> pages) {this.pages=pages;}
	void setSleepMs(int sleepMs) {this.sleepMs=sleepMs;}
	
	@Override
	public String toString() {
		return "Data: " + data + ", Pages: " + pages + ", sleepMs: " + sleepMs;
	}
}
