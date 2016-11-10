package hit.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import hit.controller.ModeController;
/**
 * the view of the select mode and the login windows.
 * @author Sharon
 *
 */
public class ModeView implements View{
	private JFrame loginFrame,modeFrame;
	private SelectModePane selectModePane;
	private AuthPane authPane;
	private ModeController controller;
	/**
	 * make mode select window load. 
	 */
	@Override
	public void open() {
		openMMUModeSelect();
	}
	/**
	 * open the mode select window - remote or local mode.
	 */
	public void openMMUModeSelect(){
		modeFrame = new JFrame("MMU Simulator");
		modeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selectModePane = new SelectModePane(this);
		selectModePane.setBackground(new Color(238,255,255));
		modeFrame.setResizable(false);
		selectModePane.setOpaque(true);                
		modeFrame.setContentPane(selectModePane);
		ImageIcon icon=new ImageIcon("docs/mmuIcon.jpg");
		modeFrame.setIconImage(icon.getImage());
		modeFrame.pack();
		modeFrame.setVisible(true);
	}
	/**
	 * open the login window.
	 */
	public void openMMULogin(){
		loginFrame = new JFrame("MMU Simulator");
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		authPane = new AuthPane(this);
		authPane.setBackground(new Color(238,255,255));
		loginFrame.setResizable(false);
		authPane.setOpaque(true);                
		loginFrame.setContentPane(authPane);
		loginFrame.setPreferredSize(new Dimension (400,200));
		ImageIcon icon=new ImageIcon("docs/mmuIcon.jpg");
		loginFrame.setIconImage(icon.getImage());
		loginFrame.pack();
		loginFrame.setVisible(true);
	}

	public JFrame getLoginFrame() {
		return loginFrame;
	}

	public JFrame getModeFrame() {
		return modeFrame;
	}
	
	public AuthPane getAuthPane() {
		return authPane;
	}
	
	public ModeController getController() {
		return controller;
	}

	public void setController(ModeController controller) {
		this.controller = controller;
	}

}
