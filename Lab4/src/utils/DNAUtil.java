package utils;

import java.util.List;

import model.Gene;

public class DNAUtil {

	
	public static String calculateResults(String gffFilepath, String fastaFilepath) throws Exception {
	
		List<Gene> genes = null;
		String dnaSequence = null;
		
		if(gffFilepath != null && gffFilepath.length() > 0) {
			genes = DNAFileReader.readGFF(gffFilepath);
		}
		
		if(fastaFilepath != null && fastaFilepath.length() > 0) {
			long start = System.nanoTime();
			dnaSequence = DNAFileReader.readFASTA(fastaFilepath);
			long stop = System.nanoTime();
			
			System.out.println("Time: " + (stop - start));
		}
		
		return "TEST";
	}
	
}
