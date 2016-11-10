package hit.memoryunits;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import hit.algorithm.FIFOAlgoImpl;
import hit.algorithm.IAlgoCache;

public class MemoryManagementUnitTest {
	private Long[] pagesToGet1=new Long[3];
	private Long[] pagesToGet2=new Long[1];
	private Long[] pagesToGet3=new Long[2];
	private boolean[] dontWrite=new boolean[3];
	
	
	@Before
	public void setUp(){
		for(int i=0;i<3;i++){
			pagesToGet1[i]=(long)(i+1);
			dontWrite[i]=false;
		}
		pagesToGet2[0]=4L;
		pagesToGet3[0]=5L;
		pagesToGet3[1]=1L;
	}
	
	@Test
	public void test() throws ClassNotFoundException, IOException{
		IAlgoCache<Long,Long> algo=new FIFOAlgoImpl<Long,Long>(3);
		MemoryManagementUnit mmu=new MemoryManagementUnit(3,algo);
		RAM ramCheck=new RAM(3);
		Page<byte[]>[] pagesRet=mmu.getPages(pagesToGet1,dontWrite);
		byte[] Page1Content=pagesRet[0].getContent(); //we would like to check that the hard disk keeps this page's content when it is kicked from the RAM
		//check when ram is empty that all the pages are returned
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesCheck1=(Page<byte[]>[]) new Page[3];
		for (int i=0;i<3;i++){
			pagesCheck1[i]=new Page<byte[]>((long)i+1,HardDisk.getInstance().pageFault((long) (i+1)).getContent());
			assertEquals(pagesRet[i].toString(),pagesCheck1[i].toString());
			ramCheck.addPage(pagesCheck1[i]);
		}
		assertEquals(ramCheck.toString(),mmu.getRam().toString());
		
		//check that PR happens as needed when RAM is full
		pagesRet=mmu.getPages(pagesToGet2,dontWrite);
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesCheck2=(Page<byte[]>[]) new Page[1];
		pagesCheck2[0]=new Page<byte[]> (4L,HardDisk.getInstance().pageFault(4L).getContent());
		assertEquals(pagesRet[0].toString(),pagesCheck2[0].toString());
		ramCheck.removePage(pagesCheck1[0]);
		ramCheck.addPage(pagesCheck2[0]);
		assertEquals(ramCheck.toString(),mmu.getRam().toString());
		
		//check that an old page that went down to HD hasn't been destroyed
		pagesRet=mmu.getPages(pagesToGet3,dontWrite);
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesCheck3=(Page<byte[]>[]) new Page[2];
		pagesCheck3[0]=new Page<byte[]> (5L,HardDisk.getInstance().pageFault(5L).getContent());
		pagesCheck3[1]=new Page<byte[]> (1L,Page1Content);
		assertEquals(pagesRet[0].toString(),pagesCheck3[0].toString());
		assertEquals(pagesRet[1].toString(),pagesCheck3[1].toString());
		ramCheck.removePage(pagesCheck1[1]);
		ramCheck.addPage(pagesCheck3[0]);
		ramCheck.removePage(pagesCheck1[2]);
		ramCheck.addPage(pagesCheck3[1]);
		assertEquals(ramCheck.toString(),mmu.getRam().toString());
	}
}
