package hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger {
	public final static String DEFAULT_FILE_NAME="Logger.txt";
	private static MMULogger instance=null;
	private FileHandler handler;
	public class OnlyMessageFormatter extends Formatter {
		@Override
		public String format (LogRecord record){
			return record.getMessage();
		}
	}
	/**
	 * all of our logs will be saved to logs/ folder in the project. this class wil manage the log.
	 */
	private MMULogger(){
		try {
			handler=new FileHandler ("logs/" + DEFAULT_FILE_NAME);
			handler.setFormatter(new OnlyMessageFormatter());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			write(e.getMessage(),Level.SEVERE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			write(e.getMessage(),Level.SEVERE);
		}
	}
	/**
	 * write the requested event to the log. each event at its time (the function is synchronized).
	 * @param command
	 * @param level
	 */
	public synchronized void write(String command, Level level) {
		LogRecord logRec=new LogRecord (level,command);
		handler.publish(logRec);
	}
	/**
	 * thaking care for singleton pattern
	 * @return
	 */
	public static MMULogger getInstance(){
		if (instance==null)
			instance=new MMULogger();
		return instance;
	}
}
