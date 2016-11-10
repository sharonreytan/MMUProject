package hit.processes;

import java.util.List;

public class RunConfiguration {
	private List<ProcessCycles> processesCycles; //input from the json file
	
	public List<ProcessCycles> getProcessesCycles(){return processesCycles;}
	public void setProcessesCycles(List<ProcessCycles> processesCycles) {
		this.processesCycles=processesCycles;
	}
	public String toString(){
		return processesCycles.toString();
	}
}
