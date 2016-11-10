package hit.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import hit.driver.MMUDriver;

@SuppressWarnings("serial")
public class AuthPane extends JPanel implements ActionListener{
	JLabel userLabel=new JLabel("Username: ");
	JLabel passLabel=new JLabel("Password: ");
	JLabel fileLabel=new JLabel("File Name: ");
	JTextField userText=new JTextField();
	JPasswordField passText=new JPasswordField();
	JTextField fileText=new JTextField();
	ModeView container;
	GridBagConstraints gbc=new GridBagConstraints();
	JButton ok=new JButton("OK");
	/**
	 * build the content of the page which contains:
	 * user name text field, password text field and a file name text field
	 * @param container
	 */
	public AuthPane(ModeView container){
		this.container=container;
		setLayout(new GridBagLayout());
		
		gbc.insets=new Insets(5,5,5,5);
		gbc.gridx=0;
		gbc.gridy=0;
		add(userLabel,gbc);
		
		gbc.gridx=1;
		userText.setPreferredSize(new Dimension(100,20));
		add(userText,gbc);
		
		gbc.gridx=0;
		gbc.gridy=1;
		add(passLabel,gbc);
		
		gbc.gridx=1;
		passText.setPreferredSize(new Dimension(100,20));
		passText.setEchoChar('*');
		add(passText,gbc);
		
		gbc.gridx=0;
		gbc.gridy=2;
		add(fileLabel,gbc);
		
		gbc.gridx=1;
		fileText.setPreferredSize(new Dimension(100,20));
		add(fileText,gbc);
		
		gbc.gridy=3;
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		add(ok,gbc);
	}
	/**
	 * ask from the controller to connect to the server, confirm the details and get the file.
	 * if the detailed are correct, open the MMUSimulator.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand()=="ok"){
			if(container.getController().callAuthenticateAndGetFile(userText.getText(), passText.getText(), fileText.getText())){
				System.out.println("sent to auth " + userText.getText() + ", "+passText.getText());
				System.out.println("filename " + fileText.getText());
				MMUDriver.openAnalyzerRemote(fileText.getText());
				this.setVisible(false);
				container.getLoginFrame().setVisible(false);
			}
			else{
				String err=container.getController().callGetErrDesc();
				switch(err){
				case("falseUser"):
					JOptionPane.showMessageDialog(container.getLoginFrame(), "User and password does not match. \nPlease try again.");
					break;
				case("falseFile"):
					JOptionPane.showMessageDialog(container.getLoginFrame(), "The file requested: " + fileText.getText()+ " was not found. \nPlease try again.");
					break;
				case("serverOff"):
					JOptionPane.showMessageDialog(container.getLoginFrame(), "Server is off. \nPlease try again.");
					break;
				default:
					JOptionPane.showMessageDialog(container.getLoginFrame(), "An error has occured. \nPlease try again.");
				}
				
				return;
			}
		}
	}

}
