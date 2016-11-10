package hit.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.net.*;

/**
 * this class is our server that waits for requests. each request will be opened as a task.
 * as long as the server is on we receive clients.
 * @author Sharon
 */
public class MMULogFileController {
	private ServerSocket server;
	private Executor executor;
	private ArrayList<MMURequest> clientReq;
/**
 * turn the server on and wait for clients.		
 */
	public void start(){
		try {
			server=new ServerSocket(12345);
			clientReq=new ArrayList<MMURequest>();		
			executor = Executors.newFixedThreadPool(15);
			System.out.println("server is on");
			while (true){
				System.out.println("waiting for clients");
				Socket client=server.accept();
				clientReq.add(new MMURequest(client));
				executor.execute(clientReq.get(clientReq.size()-1));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
