package hit.test;

import java.io.IOException;

import hit.memoryunits.MemoryManagementUnitTest;
import hit.algorithm.IAlgoCacheTest;

public class test {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		IAlgoCacheTest.test();
		new MemoryManagementUnitTest().test();
	}

}
