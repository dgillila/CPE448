package test;

import org.junit.Assert;
import org.junit.Test;

import utils.DNAConcat;

public class AllTests {

	@Test
	public void testStringCompare() {
		String s1 = "AATGCTAGT";
		String s2 = "GCTAGTCAT";
		
		for(int i = 0; i < s1.length(); i++) {
			if(DNAConcat.compareSubstrings(i, s1, s2)) {
				Assert.assertEquals(3, i);
			}
		}
	}
	
}
