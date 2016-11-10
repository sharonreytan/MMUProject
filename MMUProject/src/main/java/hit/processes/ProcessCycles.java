package hit.processes;

import java.util.List;

public class ProcessCycles {
	private List<ProcessCycle> processCycles; //list which contains Process Cycles that should be ran
	
	ProcessCycles(List<ProcessCycle> processCycles){
		this.processCycles=processCycles;
	}
	public List<ProcessCycle> getProcessCycles() {return processCycles;}
	void setProcessCycles(List<ProcessCycle> processCycles)  {
		this.processCycles=processCycles;
	}
	public String toString() {
		return "processCycles: " + processCycles;
	}
}
