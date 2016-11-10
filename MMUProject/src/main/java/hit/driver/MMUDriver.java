package hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import hit.memoryunits.HardDisk;
import hit.memoryunits.MemoryManagementUnit;
import hit.model.MMUClient;
import hit.model.MMUModel;
import hit.algorithm.*;
import hit.controller.MMUController;
import hit.controller.ModeController;
import hit.processes.Process;
import hit.processes.ProcessCycles;
import hit.processes.RunConfiguration;
import hit.util.MMULogger;
import hit.view.MMUView;
import hit.view.ModeView;

public class MMUDriver {
	@SuppressWarnings("unused")
	private String DEFAULT_FILE_NAME;
	private static int appIds=1; //counter of how many processes we have
	private static String CONFIG_FILE_NAME="‏‏Configuration.json"; //the json file name
	
	MMUDriver() throws ClassNotFoundException, IOException {
		DEFAULT_FILE_NAME=HardDisk.getInstance().getDefaultFileName();
	}
	/**
	 * this method builds the list of the processes that needs to be executed, so that runProcess can run it.
	 * @param appliocationsScenarios
	 * @param mmu 
	 * @return the list of the processes that needs to be ran
	 */
	private static List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu){
		List<Process> processes=new ArrayList<Process>();
		appIds=1;
		for(ProcessCycles i : appliocationsScenarios){
			processes.add(new Process(appIds,mmu,i)); //each process gets a unique ID
			appIds++;
		}
		MMULogger.getInstance().write("PN:" + processes.size() + "\n\n", Level.INFO);
		return processes;
	}
	/**
	 * at first, the GUI for mode select is up.
	 * if local is chosen - we run all the processes in the local json configuration file and upload the MMU Simulator.
	 * if remote is chosen - we run a login GUI and ask for a file name. If the details are correct we upload the MMUSimulator
	 * according to the requested file name. 
	 * @param args
	 */
	public static void main(String[] args) {
		MMUClient client=new MMUClient();
		ModeView modeView=new ModeView();
		ModeController modeController=new ModeController(client,modeView);
		modeController.start();
	}
	/**
	 * this method builds an object of RunConfiguration class through the Json file.
	 * the class will contain the processes that should be executed.
	 * @return RunConfiguration object with all the needed data from the file
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws FileNotFoundException
	 */
	private static RunConfiguration readConfigurationFile()  throws JsonIOException,JsonSyntaxException,FileNotFoundException{
		RunConfiguration runConfig=new Gson().fromJson(new JsonReader
				(new FileReader("src/main/resources/configuration/"+CONFIG_FILE_NAME)),RunConfiguration.class);
		return runConfig;
	}
	/**
	 * this method goes over each process and makes it start. only when all the process are done, it returns to the main, which will 
	 * operate a shutdown(maximum after 10 minutes) and opens the GUI.
	 * @param applications
	 * @throws InterruptedException
	 */
	private static void runProcesses(List<Process> applications) throws InterruptedException {
		Executor executor = Executors.newCachedThreadPool();
		for(Process i : applications){
			executor.execute(i);
		} 
		((ExecutorService) executor).shutdown();
		((ExecutorService) executor).awaitTermination(10,TimeUnit.MINUTES);
		
	}
	/**
	 * this method makes sure that after the shutdown all the RAM will be updated in the HD, so that
	 * next time the computer is up, all its files are up to date, and needed changes were saved. 
	 * @param mmu
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static void shutDown(MemoryManagementUnit mmu) throws ClassNotFoundException, IOException{
		HardDisk.getInstance().saveAll(mmu.getRam().getPages());
	}
	/**
	 * open the remote mode login GUI.
	 * @param fileName
	 */
	public static void openAnalyzerRemote(String fileName){
		MMUModel model=new MMUModel("remotelogs/"+fileName);
		MMUView view=new MMUView();
		MMUController controller=new MMUController (model,view);
		controller.start();
	}
	/**
	 * open the local mode GUI (straight to MMU Simulator).
	 * @throws InterruptedException
	 * @throws JsonIOException
	 * @throws JsonSyntaxException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void openAnalyzerLocal() throws InterruptedException, JsonIOException, JsonSyntaxException, ClassNotFoundException, IOException{
		/**
		 * Build MMU and make the processes start working, and when its done processing, it shuts down.
		 */
		int ramSize=5;
		MemoryManagementUnit mmu=new MemoryManagementUnit(ramSize,new FIFOAlgoImpl<Long,Long>(ramSize));
		MMULogger.getInstance().write("RC " + ramSize + "\n", Level.INFO);
		
		RunConfiguration runConfig=readConfigurationFile();
		List<ProcessCycles> processCycles=runConfig.getProcessesCycles();
		List<Process> processes=createProcesses(processCycles,mmu);
		
		runProcesses(processes);
		shutDown(mmu);
		/**
		 * MVC model that demonstrate the MMU running 
		 */
		MMUModel model=new MMUModel("logs/"+MMULogger.DEFAULT_FILE_NAME);
		MMUView view=new MMUView();
		MMUController controller=new MMUController (model,view);
		controller.start();
	}
}
