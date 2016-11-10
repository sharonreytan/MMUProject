package hit.controller;

import java.util.ArrayList;

import hit.model.MMUModel;
import hit.view.InfoForView;
import hit.view.MMUView;
/**
 * the controller of the MMU Simulator MVC pattern. this class will pass information
 * and requests between the model and the view
 * @author Sharon
 *
 */
public class MMUController implements Controller{
	MMUModel model;
	MMUView view;
	
	public MMUController(MMUModel model, MMUView view){
		this.model=model;
		this.view=view;
	}
	/**
	 * pass the needed parameters from the model to configure the view:
	 * sets the table size, ram capacity and processes amount.
	 */
	@Override
	public void start() {
		view.setProperties(model.getRamCapacity(), model.getDataSize(), model.getProcesses(), this);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.open();
			}
		});
	}
	/**
	 * calls the model to read the next line from the log file.
	 * @param processIds
	 * @return an object of class InfoForView that contains all the needed information for the view
	 * (e.g the process that ran, page id etc)
	 */
	public InfoForView getInfoForView (ArrayList<Integer> processIds){
		InfoForView info=model.getProcessTrace(processIds);
		return info;
	}
	/**
	 * command the model to reset the reading of the file.
	 */
	public void resetCursor(){
		model.resetFileCursor();
	}
}
