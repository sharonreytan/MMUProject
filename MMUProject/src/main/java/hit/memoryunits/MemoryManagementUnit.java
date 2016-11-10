package hit.memoryunits;
import java.io.IOException;

import hit.algorithm.*;


public class MemoryManagementUnit {
	private IAlgoCache<Long,Long> algo;
	private RAM ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo){
		ram=new RAM (ramCapacity);
		this.algo=algo;
	}
	public IAlgoCache<Long,Long> getAlgo(){
		return algo;
	}
	/**
	 * This method is used for processes that runs, and need pages from the RAM or from the HD.
	 * It manages the pages with the MMU algorithm, and alters the RAM and the HD according to the processes' requests.
	 * It returns the pages asked by the process as an array.
	 * In the returned array some fields are NULL; these fields are a flag for the calling process that it is for writing, so we efficient the writing action.
	 * 
	 * @param pageIds
	 * @param writePages
	 * @return an array of the requested pages with its ID and content
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Page<byte[]>[] getPages(Long[] pageIds,boolean[] writePages) throws IOException, ClassNotFoundException{
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesArr=(Page<byte[]>[]) new Page[pageIds.length];
		for (int i=0; i<pageIds.length; i++){
				if (algo.getElement(pageIds[i])==null){
					if(ram.isFull()){ //check if the RAM is full
						Long removeFromRam=algo.putElement(pageIds[i],pageIds[i]);//the id returned is the page that needs to be removed from RAM by the selected paging algorithm
						Page<byte[]> moveToHd=ram.getPage(removeFromRam);
						Page<byte[]> moveToRam=HardDisk.getInstance().pageReplacement(moveToHd, pageIds[i]); //update the page in the HD and upload the needed one
						ram.removePage(moveToHd); //remove the page, it is saved in the HD
						ram.addPage(moveToRam);	//upload the needed page
						if (writePages[i]==false){ //if the page is a read-only page					
							pagesArr[i]=moveToRam;	
						}
						else { //if the page is to write, therefore it will be uploaded through Process.run() method
							pagesArr[i]=null;	
						}
					}
					else{
						 Page<byte[]> moveToRam=HardDisk.getInstance().pageFault(pageIds[i]); //bring the page from the HD
						ram.addPage(moveToRam); //upload it to RAM
						algo.putElement(pageIds[i],pageIds[i]); //update the algo map
						if (writePages[i]==false){
							pagesArr[i]=moveToRam;
						}
						else
							pagesArr[i]=null;
					}
				}
				else{
					if (writePages[i]==false)
						pagesArr[i]=ram.getPage(pageIds[i]);
					else{
						pagesArr[i]=null;
					}
				}
			}
		return pagesArr;
	}
	//This method is the main method which returns array of pages that are requested from the user
	public RAM getRam() {
		return ram;
	}
	public void setAlgo(IAlgoCache<Long,Long> algo){
		this.algo=algo;
	}
	public void setRam(RAM ram){
		this.ram=ram;
	}
};
