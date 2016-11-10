package hit.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


@SuppressWarnings("serial")
public class MMUPane  extends JPanel implements ActionListener{
	JButton play, playAll, reset, chooseAll;
	JTable ramTable;
	JTextField pageFaults,pageRep;
	JLabel pageFaultsLabel, pageRepLabel, processLabel,title;
	JCheckBox[] processesToChoose;
	ArrayList<Integer> processesToShow;
	GridBagConstraints gbc=new GridBagConstraints();
	int ramCapacity;
	int dataSize;
	int processAmount;
	MMUView container;
	ArrayList<Long> ram;
	JSlider tempo;
	JScrollPane jps;
	Timer timer;
	JLabel tempoLabel;
/**
 * builds the MMU Simulator content, which includes:
 * ram table, buttons: play, reset, choose all process, play all, a check box for each process and a slider
 * to let the user choose the play all tempo.
 * @param ramCapacity
 * @param dataSize
 * @param processAmount
 * @param container
 */
	public MMUPane(int ramCapacity, int dataSize, int processAmount,MMUView container){
		this.ramCapacity=ramCapacity;
		this.dataSize=dataSize;
		this.processAmount=processAmount;
		processesToShow=new ArrayList<Integer>();
		this.container=container;
		ram=new ArrayList<Long>();
		Font fontItalic=new Font ("Ariel", Font.ITALIC, 12);
		Font fontRegular=new Font ("Ariel", Font.BOLD, 12);
		setLayout(new GridBagLayout());
		
		title=new JLabel("MMU Run Analyzer:");
		title.setFont(new Font("Book Antiqua", Font.BOLD,38));
		gbc.gridx=0;
		gbc.gridy=0;
		add(title,gbc);
		
		Long[] columns=new Long[ramCapacity];
		for (int i=0;i<ramCapacity;i++){
			columns[i]=0L;
		}
		Byte[][] data=new Byte[dataSize][ramCapacity];
		for (int i=0;i<dataSize;i++){
			for (int j=0;j<ramCapacity;j++)
				data[i][j]=0;
		}
		ramTable=new JTable(data,columns){
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
			
		};
		ramTable.setSize(new Dimension(700,80));
		ramTable.setPreferredScrollableViewportSize(new Dimension(700,80));
		ramTable.setFillsViewportHeight(true);
		ramTable.setFocusable(false);
		ramTable.setRowSelectionAllowed(false);
		ramTable.setFont(fontRegular);
		ramTable.getTableHeader().setFont(fontItalic);
		
		jps=new JScrollPane(ramTable);
		processesToChoose=new JCheckBox[processAmount];
		
		play=new JButton("Play");
		playAll=new JButton("Play All");
		play.setActionCommand("Play");
		playAll.setActionCommand("PlayAll");
		
		pageFaultsLabel=new JLabel ("Page Faults Amount: ");
		pageFaultsLabel.setFont(fontItalic);
		pageRepLabel=new JLabel ("Page Replacements Amount: ");
		pageRepLabel.setFont(fontItalic);
		processLabel=new JLabel ("Processes: ");
		
		pageFaults= new JTextField("0");
		pageRep= new JTextField("0");
		pageFaults.setEditable(false);
		pageFaults.setBackground(Color.WHITE);
		pageRep.setEditable(false);
		pageRep.setBackground(Color.WHITE);
		
		play.setEnabled(true);
		playAll.setEnabled(true);
		play.addActionListener(this);
		playAll.addActionListener(this);
		
		gbc.anchor=GridBagConstraints.LINE_START;
		gbc.gridy=1;
		gbc.gridx=0;
		gbc.gridwidth=20;
		gbc.insets=new Insets(5,5,5,5);
		add(jps,gbc);
		
		gbc.gridwidth=0;
		gbc.anchor=GridBagConstraints.WEST;
		gbc.weightx=0;
		gbc.gridy=2;
		gbc.gridx=0;
		gbc.gridwidth=1;
		add(play,gbc);
		
		gbc.gridy=2;
		gbc.gridx=1;
		add(playAll,gbc);
		
		gbc.gridx=0;
		gbc.gridy=4;
		add(pageFaultsLabel,gbc);
		
		gbc.gridx=1;
		pageFaults.setPreferredSize(new Dimension (30,20));
		add(pageFaults,gbc);
		
		gbc.gridx=0;
		gbc.gridy=5;
		add(pageRepLabel,gbc);
		
		gbc.gridx=1;
		pageRep.setPreferredSize(new Dimension (30,20));
		add(pageRep,gbc);
		
		gbc.gridy=2;
		gbc.gridx=19;
		add(processLabel,gbc);
		
		int processY=3;
		for (int i=0;i<processAmount;i++){
			gbc.gridy=processY;
			processesToChoose[i]=new JCheckBox();
			add(processesToChoose[i],gbc);
			processesToChoose[i].setActionCommand("Chosen P"+(i+1));
			processesToChoose[i].addActionListener(this);
			processesToChoose[i].setText("Process " + (i+1));
			processesToChoose[i].setBackground(new Color(238,255,255));
			processesToChoose[i].setFont(fontRegular);
			processY++;
		}
		
		chooseAll=new JButton("Choose All Processes");
		chooseAll.addActionListener(this);
		chooseAll.setActionCommand("Choose All Processes");
		gbc.gridy=processesToChoose.length+3;
		gbc.gridx=19;
		gbc.gridwidth=1;
		add(chooseAll,gbc);
		gbc.gridy=8;
		gbc.gridx=0;
		tempoLabel=new JLabel ("Play All Tempo: ");
		tempoLabel.setBackground(new Color(238,255,255));
		add(tempoLabel, gbc);
		
		tempo = new JSlider(JSlider.HORIZONTAL, 0, 10, 1);
		tempo.setMinorTickSpacing(1);
		tempo.setMajorTickSpacing(3);
		tempo.setPaintTicks(true);
		tempo.setPaintLabels(true);
		tempo.setLabelTable(tempo.createStandardLabels(2));
		tempo.setToolTipText("10 for second per play,\n 0 for almost instant final result");
		tempo.setFont(fontItalic);
		tempo.setBackground(new Color(200,220,255));
		gbc.gridy=9;
		gbc.gridx=0;
	    add(tempo, gbc);
	    
	    reset = new JButton ("Reset");
	    reset.addActionListener(this);
	    reset.setActionCommand("reset");
	    play.setPreferredSize(reset.getPreferredSize());
	    gbc.gridy=3;
	    add(reset, gbc);	
	}
	/**
	 * listen to the buttons on the panel.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="Play"){
			if(!container.getInfoFromController())
				JOptionPane.showMessageDialog(container.getAnalyzerFrame(), "End of log file. Press Reset to start over.");
			return;
		}
		if(e.getActionCommand()=="PlayAll"){
			timer = new Timer(tempo.getValue()*100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {    
                	if (!container.getInfoFromController()){
                		timer.stop();
                		JOptionPane.showMessageDialog(container.getAnalyzerFrame(), "End of log file. Press Reset to start over.");
                		return;
                	}
                    repaint();                    
                }
            });
			timer.start();
			return;
		}
		if(e.getActionCommand().contains("Chosen")){
			int processChosen=Integer.parseInt(e.getActionCommand().substring(8));
			if (processesToChoose[processChosen-1].isSelected()){
				processesToShow.add(processChosen);
			}
			else{
				processesToShow.remove(processesToShow.indexOf(processChosen));
			}
			return;
		}
		if(e.getActionCommand()=="Choose All Processes"){
			int i=1;
			for(JCheckBox proc : processesToChoose){
				proc.setSelected(true);
				processesToShow.add(i);
				i++;
			}
			return;
		}
		if(e.getActionCommand()=="reset"){
			container.resetCursor();
		}
	}


	public JTable getRamTable() {
		return ramTable;
	}


	public JTextField getPageFaults() {
		return pageFaults;
	}


	public JTextField getPageRep() {
		return pageRep;
	}


	public ArrayList<Integer> getProcessesToShow() {
		return processesToShow;
	}
	
	public ArrayList<Long> getRam() {
		return ram;
	}
}
