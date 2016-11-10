package hit.view;
/**
 * this class is a data structure for all the needed information for each event of the MMU run log file.
 * @author Sharon
 *
 */
public class InfoForView {
	public enum LOG_EVENT{PAGE_FAULT,PAGE_REPLACEMENT,GET_PAGES,ERROR};
	public LOG_EVENT logEvent;
	public Long mth;
	public Long mtr;
	public Byte[] data;
	public Long pageId;
	public int processId;
	private boolean endOfInfo=false;
	
	public Long getMth() {
		return mth;
	}
	public Long getMtr() {
		return mtr;
	}
	public Byte[] getData() {
		return data;
	}
	public Long getPageId() {
		return pageId;
	}
	public void setLogEvent(LOG_EVENT logEvent) {
		this.logEvent = logEvent;
	}
	public void setMth(Long mth) {
		this.mth = mth;
	}
	public void setMtr(Long mtr) {
		this.mtr = mtr;
	}
	public void setData(Byte[] data) {
		this.data = data;
	}
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
	public LOG_EVENT getLogEvent() {
		return logEvent;
	}
	public int getProcessId() {
		return processId;
	} 
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public void setEndOfInfo(boolean endOfInfo) {
		this.endOfInfo = endOfInfo;
	}
	public boolean getEndOfInfo() {
		return endOfInfo;
	}
}
