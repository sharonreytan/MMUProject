package hit.model;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * A class which represents the client side in remote mode. it will hold all the needed information from the server,
 * which are the file and the authentication, or failure reason.
 * @author Sharon
 *
 */
public class MMUClient implements Model {
	private Socket server;
	private String msgIn;
	private String errorDesc=null;
	boolean isConnect=false;
	/**
	 * connect to the server
	 */
	@Override
	public void readData() {
		try {
			InetAddress address=InetAddress.getLocalHost();
			server = new Socket(address,12345);
			isConnect=true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * connect to the server, send it the user, password and file name requested.
	 * if these details are correct, we download the file from the server and save it to remotelogs/ in the project.
	 * if something went wrong it is saved in the errDesc field as a string.
	 * @param user
	 * @param pass
	 * @param fileName
	 * @return true: if the authentication and file upload succeeded. else, false. 
	 * @throws IOException
	 */
	public boolean authenticateAndGetFile(String user, String pass, String fileName) throws IOException{
		if (!isConnect){
			readData();
			if (!isConnect){
				errorDesc="serverOff";
				return false;
			}
		}
		boolean fileRead=false;
		int bytesRead;
	    int current = 0;
	    FileOutputStream fos = null;
	    BufferedOutputStream bos = null;
	    ObjectOutputStream output = null;
	    ObjectInputStream input = null;
		try {
			output=new ObjectOutputStream(server.getOutputStream());
			input=new ObjectInputStream(server.getInputStream());
			
			output.writeObject(user + " " + pass + " " + fileName);
			msgIn=(String)input.readObject();
			System.out.println("received from server: " + msgIn);
			if (msgIn.contains("false")){
				errorDesc=msgIn;
				server.close();
				output.close();
				input.close();
				isConnect=false;
				return false;
			}
			InputStream is = server.getInputStream();
			byte [] fileBytes  = new byte [6022386];
			bytesRead = is.read(fileBytes,0,fileBytes.length);
			
		    fos = new FileOutputStream("remotelogs/"+fileName);
		    bos = new BufferedOutputStream(fos);
		    current = bytesRead;

		      do {
		         bytesRead = is.read(fileBytes, current, (fileBytes.length-current));
		         if(bytesRead >= 0) 
		        	 current += bytesRead;
		      } while(bytesRead > -1);
		      
		      bos.write(fileBytes, 0 , current);
		      bos.flush();
		      fileRead=true;
		      isConnect=false;
	    	  server.close(); 
	    	  input.close(); 
	    	  output.close();
		    } catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		    finally {
		      if (fos != null) 
		    	  fos.close();
		      if (bos != null) 
		    	  bos.close();
		    }
		return fileRead;
	}
	/**
	 * 
	 * @return a description of the error that occured while connecting to the server.
	 */
	public String getErrorDesc() {
		return errorDesc;
	}
}

