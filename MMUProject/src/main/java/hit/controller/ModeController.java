package hit.controller;

import java.io.IOException;

import hit.model.MMUClient;
import hit.view.ModeView;

public class ModeController implements Controller{
	ModeView view;
	MMUClient model;
	
	public ModeController(MMUClient model,ModeView view){
		this.view=view;
		this.model=model;
		view.setController(this);
	}
	@Override
	public void start() {
		view.open();
	}
	
	public boolean callAuthenticateAndGetFile(String user, String pass, String fileName){
		
		try {
			return model.authenticateAndGetFile(user, pass, fileName);
		} catch (IOException e) {
			return false; //fix this put an error that server is down or something
		}
	}
	
	public String callGetErrDesc(){
		return model.getErrorDesc();
	}
}
