package hit.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import hit.controller.MMUController;

public class MMUView implements View{
	int ramCapacity;
	int dataSize;
	int processAmount;
	MMUController controller;
	MMUPane pane;
	JFrame analyzerFrame;
	/**
	 * Open the MMU Simulator and its contents.
	 */
	@Override
	public void open() {
		analyzerFrame = new JFrame("MMU Simulator");
		analyzerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = new MMUPane(ramCapacity,dataSize,processAmount, this);
		pane.setBackground(new Color(238,255,255));
		analyzerFrame.setResizable(false);
		pane.setOpaque(true);                
		analyzerFrame.setContentPane(pane);
		analyzerFrame.setPreferredSize(new Dimension (1000,600));
		ImageIcon icon=new ImageIcon("docs/mmuIcon.jpg");
		analyzerFrame.setIconImage(icon.getImage());
		analyzerFrame.pack();
		analyzerFrame.setVisible(true);
	}

	public JFrame getAnalyzerFrame() {
		return analyzerFrame;
	}
	/**
	 * reset the needed parametets for the view to load.
	 * @param ramCapacity
	 * @param dataSize
	 * @param processAmount
	 * @param controller
	 */
	public void setProperties(int ramCapacity,int dataSize,int processAmount, MMUController controller) {
		this.ramCapacity = ramCapacity;
		this.dataSize = dataSize;
		this.processAmount = processAmount;
		this.controller=controller;
	}
	/**
	 * get the info of what happened in the next line in the log from the controller, and change the view as needed,
	 * according to the event that happened.
	 * @return true - if there is more lines in the log to read
	 * false - if we reached the end of the log file
	 */
	public boolean getInfoFromController(){
		InfoForView info = controller.getInfoForView(pane.getProcessesToShow());
		if (info==null)
			return false;
		if (info.getEndOfInfo())
			return false;
		switch(info.getLogEvent()){
		case PAGE_FAULT:
			pane.getPageFaults().setText(Integer.toString(Integer.parseInt(pane.getPageFaults().getText())+1));
			break;
		case PAGE_REPLACEMENT:
			pane.getPageRep().setText(Integer.toString(Integer.parseInt(pane.getPageRep().getText())+1));
			if(pane.getRam().contains(info.getMth())){
				int colToRemove=pane.getRam().indexOf(info.getMth());
				JTableHeader th = pane.getRamTable().getTableHeader();
				TableColumnModel tcm = th.getColumnModel();
				TableColumn tc = tcm.getColumn(colToRemove);
				tc.setHeaderValue("0");
				Byte zero=0;
				for (int i=0;i<dataSize;i++){
					pane.getRamTable().setValueAt(zero, i, colToRemove);
				}
				th.repaint();
				pane.getRamTable().repaint();
				pane.getRam().set(colToRemove, -1L);
			}
			break;
		case GET_PAGES:
			if(pane.getProcessesToShow().contains(info.getProcessId())){
				if(pane.getRam().contains(info.getPageId())){
						int j=pane.getRam().indexOf(info.getPageId());
						int i=0;
						for (Byte currByte : info.getData()){
							pane.getRamTable().setValueAt(currByte, i, j);
							i++;
						}
				}
				else{
					if (pane.getRam().contains(-1L)){
						pane.getRam().set(pane.getRam().indexOf(-1L), info.getPageId());
					}
					else
						pane.getRam().add(info.getPageId());
					int j=pane.getRam().indexOf(info.getPageId());
					JTableHeader th = pane.getRamTable().getTableHeader();
					TableColumnModel tcm = th.getColumnModel();
					TableColumn tc = tcm.getColumn(pane.getRam().indexOf(info.getPageId()));
					tc.setHeaderValue(info.getPageId().toString());
					th.repaint();
					int i=0;
					for (Byte currByte : info.getData()){
						pane.getRamTable().setValueAt(currByte, i, j);
						i++;
					}
					pane.getRamTable().repaint();
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	/**
	 * ask the controller to read the log file from the controller.
	 */
	public void resetCursor(){
		controller.resetCursor();
		pane.getPageFaults().setText("0");
		pane.getPageRep().setText("0");
		pane.getRam().clear();
		Byte zero=0;
		JTableHeader th = pane.getRamTable().getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		for(int i=0;i<ramCapacity;i++){
			tcm.getColumn(i).setHeaderValue("0");
			for (int j=0;j<dataSize;j++)
				pane.getRamTable().setValueAt(zero, j, i);
		}
		th.repaint();
		pane.getRamTable().repaint();
	}
}
