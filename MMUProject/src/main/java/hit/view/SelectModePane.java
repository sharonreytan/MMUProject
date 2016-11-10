package hit.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import hit.driver.MMUDriver;

@SuppressWarnings("serial")
public class SelectModePane extends JPanel implements ActionListener{
	enum CHOSEN_MODE {LOCAL,REMOTE};
	CHOSEN_MODE chosen=CHOSEN_MODE.LOCAL;
	ModeView container;
	JRadioButton local=new JRadioButton("Local mode");
	JRadioButton remote=new JRadioButton("Remote mode");
	JLabel select=new JLabel("Please select a mode. \nRemote to upload you own file (requires logging in) \nor Local to run local files.");
	GridBagConstraints gbc=new GridBagConstraints();
	JButton ok=new JButton("OK");
	/**
	 * build the content of the mode selection window, which conatins two radio buttons, one for each mode, and an OK button.
	 * @param container
	 */
	public SelectModePane(ModeView container){
		this.container=container;
		setLayout(new GridBagLayout());
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.weightx=3;
		gbc.gridwidth=3;
		add(select,gbc);
		
		local.setActionCommand("local");
		local.addActionListener(this);
		local.setBackground(new Color(238,255,255));
		local.setSelected(true);
		gbc.gridwidth=1;
		gbc.gridy=1;
		gbc.insets=new Insets(5,5,5,5);
		add(local,gbc);
		
		remote.setActionCommand("remote");
		remote.addActionListener(this);
		remote.setBackground(new Color(238,255,255));
		gbc.gridx=1;
		add(remote,gbc);
		
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		gbc.gridx=0;
		gbc.gridy=2;
		add(ok,gbc);
	}
	/**
	 * listen to the radio buttons and the OK button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch(arg0.getActionCommand()){
		case("local"):
			if(remote.isSelected())
				remote.setSelected(false);
			chosen=CHOSEN_MODE.LOCAL;
			break;
		case("remote"):
			if(local.isSelected())
				local.setSelected(false);
			chosen=CHOSEN_MODE.REMOTE;
			break;
		case("ok"):
			this.setVisible(false);
			container.getModeFrame().setVisible(false);
			switch(chosen){
			case LOCAL:
				try{
					MMUDriver.openAnalyzerLocal();
				}catch (Exception ex){
					ex.printStackTrace();
				}
				break;
			case REMOTE:
				container.openMMULogin();
				break;
			}
			break;
		}
		
	}

	public CHOSEN_MODE getChosen() {
		return chosen;
	}

}
