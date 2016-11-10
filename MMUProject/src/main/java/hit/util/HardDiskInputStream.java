package hit.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import hit.memoryunits.Page;
/**
 * this class reads from the hard disk file which files are in it
 * @author Sharon
 *
 */
public class HardDiskInputStream extends ObjectInputStream {
	
	public HardDiskInputStream(InputStream in) throws IOException {
		super(in);
		}

	public Map<Long,Page<byte[]>> readAllPages() throws IOException, ClassNotFoundException{
		@SuppressWarnings("unchecked")
		Map<Long,Page<byte[]>> allPages=(HashMap<Long,Page<byte[]>>) readObject(); 
		return allPages;
	}
}
