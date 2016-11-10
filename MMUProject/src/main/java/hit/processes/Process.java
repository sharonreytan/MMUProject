package hit.processes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import hit.memoryunits.MemoryManagementUnit;
import hit.memoryunits.Page;
import hit.util.MMULogger;

public class Process implements Runnable{
	private int id;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles){
		this.id=id;
		this.mmu=mmu;
		this.processCycles=processCycles;
	}
	public int getId() {return id;}
	public void setId(int id) {this.id=id;}
	/**
	 * this method makes each of the process cycle in processCycles to request its pages, update them and then 
	 * go to sleep. their common source is the MMU, which synch the RAM and the HD.
	 */
	@Override
	public void run() {
		List<ProcessCycle> processCyclesList=processCycles.getProcessCycles();
		for (ProcessCycle i : processCyclesList){
			synchronized (mmu){
				Long[] pageArr=new Long[i.getPages().size()];
				for (int j=0;j<i.getPages().size();j++)
					pageArr[j]=i.getPages().get(j);
				boolean[] writePage=new boolean[pageArr.length];
				for (int j=0;j<pageArr.length;j++){
					if (i.getData().get(j).length==0)
						writePage[j]=false;
					else
						writePage[j]=true;
				}
				try { 
					Page<byte[]>[] pages=mmu.getPages(pageArr,writePage);	
					for (int j=0;j<pages.length;j++){
						if (pages[j]==null){
							if (mmu.getRam().getPages().containsKey(pageArr[j])){
								mmu.getRam().getPages().get(pageArr[j]).setContent(i.getData().get(j));
							}
						}
						if (mmu.getRam().getPages().get(pageArr[j]) != null) //in case that the page was not uploaded to the RAM (e.g if we used LFU, some pages might not have been loaded)
							MMULogger.getInstance().write("GP:P" + id + " " +pageArr[j]+" "+Arrays.toString(mmu.getRam().getPages().get(pageArr[j]).getContent())+"\n",Level.INFO);
					}
				} 
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				
	
				try{
					Thread.sleep(i.getSleepMs());
				}
				catch (InterruptedException e) {e.printStackTrace();}
			}
		}
	}
}
