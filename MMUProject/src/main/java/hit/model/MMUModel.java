package hit.model;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import hit.util.MMULogger;
import hit.view.InfoForView;

public class MMUModel implements Model{
	private int ramCapacity;
	private int processes;
	private int dataSize;
	private BufferedReader logger;
	private ArrayList<String> fileContent;
	int currIndex;
	
	public MMUModel(String fileNamePath){
		try {
			FileReader fr=new FileReader(fileNamePath);
			logger=new BufferedReader (fr);
			currIndex=0;
			fileContent=new ArrayList<String>();
			readData();
			logger.close();
			fr.close();
			currIndex=3; //move to relevant log lines in the file (after RC and PN)
		}catch (FileNotFoundException e) {
				MMULogger.getInstance().write("No Log File", Level.SEVERE);
			}catch (IOException e) {
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
	}
	/**
	 * this function reads the whole file requested at its path, and saves it in order to manage efficiently.
	 * it also saves the required basic info to upload the view (ram size, processes amount and the size of the data).
	 * @param fileNamePath
	 */
	@Override
	public void readData() {
			try{
				String currLine=logger.readLine();
				while (currLine != null){
					fileContent.add(currIndex, currLine);
					currIndex++;
					currLine=logger.readLine();
				}				
				ramCapacity=Integer.parseInt(fileContent.get(0).substring(3));
				processes=Integer.parseInt(fileContent.get(1).substring(3));
				for (int i=4;i<fileContent.size();i++){ //when GP might happen is at row 4
					if(fileContent.get(i).contains("GP")){
						String currDataStr=fileContent.get(i).substring(fileContent.get(i).indexOf("[")+1,fileContent.get(i).indexOf("]"));
						currDataStr=currDataStr.replace(", ", " ");
						currDataStr=currDataStr+" ";
						dataSize=0;
						for (int j=0;j<currDataStr.length();j++){
							if (currDataStr.charAt(j)==' ')
								dataSize++;
						}
						i=fileContent.size();
					}
				}
			}
			catch (IOException e){
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
			}	
	}
	/**
	 * reads the next line in the log file, its type and its information.
	 * @param processIds
	 * @return a class that holds all the needed information that the controller will provide the view.
	 */
	public InfoForView getProcessTrace(ArrayList<Integer> processIds){
		InfoForView info=new InfoForView();
		if (fileContent.size()<=currIndex){
			info.setEndOfInfo(true);
			return info;
		}
		String currLine=fileContent.get(currIndex);
		currIndex++;
		if (currLine.contains("PF")){
			info.setLogEvent(InfoForView.LOG_EVENT.PAGE_FAULT);
			return info;
		}
		if (currLine.contains("PR")){
			info.setLogEvent(InfoForView.LOG_EVENT.PAGE_REPLACEMENT);
			info.setMth(Long.parseLong(currLine.substring(7, currLine.indexOf(" ", 7))));
			info.setMtr(Long.parseLong(currLine.substring(currLine.indexOf("MTR ")+"MTR ".length())));
			return info;
		}
		if (currLine.contains("GP")){
			info.setLogEvent(InfoForView.LOG_EVENT.GET_PAGES);
			int currProcessId=Integer.parseInt(currLine.substring(4, currLine.indexOf(" ")));
			info.setProcessId(currProcessId);
			if (processIds.contains(currProcessId)){
				Long currPageId=Long.parseLong(currLine.substring(currLine.indexOf(" ")+1,currLine.indexOf(" ", 6)));
				info.setPageId(currPageId);
				String currDataStr=currLine.substring(5+Integer.toString(currProcessId).length()+Long.toString(currPageId).length());
				currDataStr=currDataStr.replace(", ", " ");
				currDataStr=currDataStr.replace(" [", "");
				currDataStr=currDataStr.replace("]", "");
				currDataStr=currDataStr+" ";
				Byte[] currData=new Byte[dataSize];
				int j=0;
				int spaceIndex;
				for (int i=0;i<currDataStr.length();i++){
					spaceIndex=currDataStr.indexOf(" ", i);
					currData[j]=Byte.parseByte(currDataStr.substring(i, spaceIndex));
					i=spaceIndex;
					j++;
				}
				info.setData(currData);
			}
			return info;
		}
		info.setLogEvent(InfoForView.LOG_EVENT.ERROR);
		return info;
	}
	/**
	 * move the file cursor to the file's beginning
	 */
	public void resetFileCursor(){
		currIndex=3;
	}
	
	public int getRamCapacity() {
		return ramCapacity;
	}


	public int getProcesses() {
		return processes;
	}
	
	public int getDataSize(){
		return dataSize;
	}

}
