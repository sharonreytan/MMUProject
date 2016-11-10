package hit.algorithm;

import hit.memoryunits.Page;
import hit.memoryunits.RAM;

public class IAlgoCacheTest {
	public static void test(){
		System.out.println("Test Page & RAM");
		byte[] byteArr1={(byte)1,(byte)2,(byte)3};
		byte[] byteArr2={(byte)4,(byte)5,(byte)6};
		Page<byte[]> tester=new Page<byte[]> (1L,byteArr1);
		Page<byte[]> tester1=new Page<byte[]> (2L,byteArr2);
		System.out.println(tester);
		System.out.println(tester.equals(tester1));
		
		RAM testRAM=new RAM(2);
		
		testRAM.addPage(tester);
		testRAM.addPage(tester1);
		
		System.out.println(testRAM);
		System.out.println("----------------------------------------");
		System.out.println("Test FIFOAlgoImpl");
		FIFOAlgoImpl<Integer,Integer> fifoTest= new FIFOAlgoImpl<Integer,Integer>(3);
		fifoTest.putElement (7, 3) ;
		fifoTest.putElement (8, 4) ;
		fifoTest.putElement (9, 5) ;
		System.out.println("Test getElement (should be 3):");
		System.out.println(fifoTest.getElement(7));
		System.out.println("Test putElement -> full capacity (should be [8, 9, 6]):");
		fifoTest.putElement (6, 1) ;
		System.out.println(fifoTest);
		System.out.println("Test removeElement (should be [8, 9]:");
		fifoTest.removeElement(6);
		System.out.println(fifoTest);
		System.out.println("----------------------------------------");
		System.out.println("Test LFUAlgoImpl");
		LFUAlgoCacheImpl<Integer,Integer> lfuTest= new LFUAlgoCacheImpl<Integer,Integer>(3);
		lfuTest.putElement (7, 3) ;
		lfuTest.putElement (8, 4) ;
		lfuTest.putElement (9, 5) ;
		System.out.println("Test getElement (should be 5):");
		System.out.println(lfuTest.getElement(9));
		System.out.println("Test getElement changing the order of the array by previous getElement (should be {7=1, 8=1, 9=2}:");
		System.out.println(lfuTest);
		System.out.println("Test putElement -> full capacity (should be {6=1, 8=1, 9=2}):");
		lfuTest.putElement (6, 1) ;
		System.out.println(lfuTest);
		System.out.println("Test removeElement (should be {6=1, 9=2}):");
		lfuTest.removeElement(8);
		System.out.println(lfuTest);
		System.out.println("----------------------------------------");
		System.out.println("Test LRUAlgoImpl");
		LRUAlgoCacheImpl<Integer,Integer> lruTest= new LRUAlgoCacheImpl<Integer,Integer>(3);
		lruTest.putElement (7, 3) ;
		lruTest.putElement (8, 4) ;
		lruTest.putElement (9, 5) ;
		System.out.println(lruTest);
		System.out.println("Test getElement (should be 4):");
		System.out.println(lruTest.getElement(8));
		System.out.println("Test getElement changing the order of the array by previous getElement (should be [8, 9, 7]):");
		System.out.println(lruTest);
		System.out.println("Test putElement -> full capacity (should be [6, 8, 9]):");
		lruTest.putElement (6, 1) ;
		System.out.println(lruTest);
		System.out.println("Test removeElement (should be [6, 8]):");
		lruTest.removeElement(9);
		System.out.println(lruTest);
	}
}
