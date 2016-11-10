package hit.memoryunits;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.Map.Entry;

import hit.util.*;

public class HardDisk {
	private Map<Long,Page<byte[]>> allPages;
	static int _SIZE; 
	static String DEFAULT_FILE_NAME="HardDiskContent.dat";
	private static HardDisk instance=null;
	
	private HardDisk() throws IOException, ClassNotFoundException{
		try{
			FileReader fr = new FileReader ("src/main/resources/harddisk/"+"HardDiskConfig.dat");
			BufferedReader myFile=new BufferedReader (fr);
			_SIZE=Integer.parseInt(myFile.readLine().substring("_SIZE=".length()));
			myFile.close();
			fr.close();
		}
		catch (IOException e){
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
		}
		try{
			HardDiskInputStream hdis=new HardDiskInputStream(new FileInputStream("src/main/resources/harddisk/"+DEFAULT_FILE_NAME));
			allPages=hdis.readAllPages();//reset the pages in the HD map, so we will not need to read the file with a stream each access
			hdis.close();
		}
		catch (IOException ieo){
			firstLoad();//in the first upload, the HD does not exist. We create pages into it.
			MMULogger.getInstance().write(ieo.toString(), Level.SEVERE);
		}
	}
	public static HardDisk getInstance() throws IOException, ClassNotFoundException{
		if (instance==null)
			instance=new HardDisk();//singleton pattern
		return instance;
	}
	public Page<byte[]> pageFault(Long pageId) throws IOException{
		MMULogger.getInstance().write("PF " + pageId+"\n", Level.INFO);
		return allPages.get(pageId);
	}
	//This method is called when a page is not in fast memory (RAM)
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws IOException{
		allPages.remove(moveToHdPage.getPageId());
		allPages.put(moveToHdPage.getPageId(), moveToHdPage);//update the page data in the map
		writeHd();//update the HD 
		MMULogger.getInstance().write("PR:MTH " + moveToHdPage.getPageId()+" MTR "+moveToRamId+"\n",  Level.INFO);
		return allPages.get(moveToRamId);//return the page that will be uploaded to the RAM
	}
	/**
	 * this method saves all the pages from the given list, which contains the files in the RAM and its content,
	 * into the HD.
	 * @param toSave
	 * @throws IOException
	 */
	public void saveAll(Map<Long,Page<byte[]>> toSave) throws IOException{
		Set<Entry<Long, Page<byte[]>>> entries=toSave.entrySet();
		for (Entry<Long, Page<byte[]>> entry : entries) { 
            allPages.remove(entry.getKey());
            allPages.put(entry.getKey(), entry.getValue()); //update the page content
        }
		writeHd(); //update the hard disk file
	}
	//This method is called when a page is not in fast memory (RAM) and RAM is also with full capacity
	/**
	 * this method writes all the files stored in the hard disk to the file
	 * @throws IOException
	 */
	private void writeHd() throws IOException{
		HardDiskOutputStream hdos = new HardDiskOutputStream (new FileOutputStream ("src/main/resources/harddisk/"+"HardDiskContent.dat"));
		try{
			hdos.writeAllPages(allPages); 
			
		}
		catch (IOException ieo){
			MMULogger.getInstance().write(ieo.toString(), Level.SEVERE);
		}
		finally {
			hdos.close();
		}
	}
	public String getDefaultFileName(){
		return DEFAULT_FILE_NAME;
	}
	private void firstLoad() throws IOException{
		allPages=new HashMap<Long,Page<byte[]>>();
		byte[] b = null;
		for (int i=0;i<1001;i++){
			b = new byte[5];
			new Random().nextBytes(b);
			allPages.put((long)i, new Page<byte[]>((long)i,b));
		}
		writeHd();
	}
	//this method creates 1001 random pages to the HD with ids from 0 to 1000
}
