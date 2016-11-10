package hit.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import hit.memoryunits.Page;
/**
 * this class writes to the hard disk file its content
 * @author Sharon
 *
 */
public class HardDiskOutputStream extends ObjectOutputStream{

	public HardDiskOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	public void writeAllPages(Map<Long,Page<byte[]>> hd) throws IOException{
 		writeObject(hd);
	}
}
