package hit.applicationservice;

import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * this class is responsible to map all the files in the logcache directory, and pass it to the client.
 * @author Sharon
 *
 */
public class MMULogService {
	private Socket socket;
	private String fileName;
	private ArrayList<String> fileNameSpace;
	
	public MMULogService(Socket socket, String fileName) throws IOException{ 
		this.socket=socket;
		this.fileName=fileName;
		createListFile();
	}
	/**
	 * upload the file to the client
	 * @return true - if the reading succeeded.
	 * false - if the reading failed.
	 * @throws IOException
	 */
	public boolean readFileReq() throws IOException{
		boolean readFile=false;
		FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
		try{
			File fil = new File ("src/main/resources/logcache/"+fileName);
			System.out.println("reading...");
	        byte [] fileBytes  = new byte [(int)fil.length()];
	        fis = new FileInputStream(fil);
	        bis = new BufferedInputStream(fis);
	        bis.read(fileBytes,0,fileBytes.length);
	        os = socket.getOutputStream();
	        os.write(fileBytes,0,fileBytes.length);
	        os.flush();
	        readFile=true;
        	socket.close(); 
        	bis.close();
        	os.close();
	        }catch (IOException e){
				e.printStackTrace();
			}	
		return readFile;
	}
	/**
	 * map all the files in the logcache directory
	 */
	public void createListFile(){
		String dirName=System.getProperty("user.dir")+"\\src\\main\\resources\\logcache";
		fileNameSpace=new ArrayList<String>();
		File dir = new File(dirName);
		File [] files=dir.listFiles();
		for (File f : files){
			fileNameSpace.add(f.getName());
		}
		System.out.println("files on the server: " + fileNameSpace);
	}
	/**
	 * @return true - if the files exists in logcache/
	 * false - if there is no file as requested in the log cache.
	 */
	public boolean isFileExist(){
		if (fileNameSpace.contains(fileName))
			return true;
		return false;
	}
}
