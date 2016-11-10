package hit.login;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Authentication {
	private static String DEFAULT_FILE_NAME="users.txt";
	private static HashMap<String,String> users;
	private static Authentication instance=null;
	/**
	 * The constructor reads the users and their passwords in order to store it
	 */
	private Authentication(){
		FileReader fr;
		users=new HashMap<String,String>();
		try {
			fr = new FileReader("src/main/resources/users/"+DEFAULT_FILE_NAME);
			BufferedReader usersInfo=new BufferedReader (fr);
			String currUser;
			String currPass;
			String currLine=usersInfo.readLine();
			while (currLine != null){
				currUser=currLine.substring(0, currLine.indexOf(" "));
				currPass=currLine.substring(currLine.indexOf(" ")+1);
				users.put(currUser, currPass);
				currLine=usersInfo.readLine();
			}
			System.out.println("the users: " + users);
			usersInfo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * This method searches for a user name and its password, in order to return
	 * if there is a match or not. 
	 * @param user
	 * @param password
	 * @return true - if there is a match, otherwise false
	 */
	public boolean authenticate(String user, String password) {
		if(users.containsKey(user)){
			if (password.equals(users.get(user)))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	public static Authentication getInstance(){
		if (instance==null)
			instance=new Authentication();
		return instance;
	}
}
