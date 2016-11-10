package hit.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import hit.applicationservice.MMULogService;
import hit.login.Authentication;
/**
 * this class takes care of each client the connects to our server.
 * @author Sharon
 *
 */
public class MMURequest implements Runnable{
	Socket myClient;
	
	public MMURequest(Socket myClient){
		this.myClient=myClient;
	}
	/**
	 * start taking care of the client connected - confirm that his user name and password are registered,
	 * and if the file exits upload it to him. if an error occurred - return a description what is wrong.
	 */
	@Override
	public void run() {
		try {
			ObjectOutputStream output=new ObjectOutputStream(myClient.getOutputStream());
			ObjectInputStream input=new ObjectInputStream(myClient.getInputStream());
			
			String msgIn=(String)input.readObject();
			String user=msgIn.substring(0, msgIn.indexOf(' '));
			String pass=msgIn.substring(msgIn.indexOf(' ')+1, msgIn.indexOf(" ", user.length()+1));
			String file=msgIn.substring(user.length() + pass.length() + 2);
			System.out.println("client is in. msg from it: " + msgIn);
			System.out.println("user: " + user);
			System.out.println("password: " + pass);
			System.out.println("file: " + file);
			boolean isAuth=Authentication.getInstance().authenticate(user, pass);
			MMULogService logServ=new MMULogService(myClient,file);
			boolean isFileExist=logServ.isFileExist();
			if (isAuth && isFileExist){
				output.writeObject("ok");
				System.out.println("auth success and file exists. now reads file...");
				logServ.readFileReq();
			}
			else if (!isAuth){
				output.writeObject("falseUser");
				output.flush();
			}
			else if (!isFileExist){
				output.writeObject("falseFile");
				output.flush();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
