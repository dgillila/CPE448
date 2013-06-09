package test;

import org.junit.Assert;
import org.junit.Test;

import utils.DNAConcat;

public class AllTests {

	@Test
	public void testStringCompare() {
		StringBuilder s1 = new StringBuilder("AATGCTAGT");
		StringBuilder s2 = new StringBuilder("GCTAGTCAT");
		
		for(int i = 0; i < s1.length(); i++) {
			if(DNAConcat.compareSubstrings(i, s1, s2)) {
				Assert.assertEquals(3, i);
			}
		}
	}
	
	@Test
	public void testStringMerge() {
		StringBuilder s1 = new StringBuilder("AATGCTAGT");
		StringBuilder s2 = new StringBuilder("GCTAGTCAT");
		
		int overlap = DNAConcat.mergeStrings(s1, s2);
		
		Assert.assertEquals(6, overlap);
		Assert.assertEquals("AATGCTAGTCAT", s1.toString());
	}
	
	@Test
	public void testApplication() {
		try {
//			DNAConcat.processFiles("C:\\Users\\Daniel\\Documents\\D_erecta_2nd_3L_control_fasta1.zip", "C:\\Users\\Daniel\\Documents\\D_erecta_2nd_3L_control_gff1.zip");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testGlobalAlign() {
		Assert.assertEquals(1, DNAConcat.globalAlign("test", "not close", "test"));
		Assert.assertEquals(20, DNAConcat.globalAlignHelper("test", "test"));
		Assert.assertEquals(11, DNAConcat.globalAlignHelper("test", "tes"));
		Assert.assertEquals(11, DNAConcat.globalAlignHelper("test", "tesf"));
		Assert.assertEquals(11, DNAConcat.globalAlignHelper("tes", "test"));
	}
}
