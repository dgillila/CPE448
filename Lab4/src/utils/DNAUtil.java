package utils;

import java.util.List;

import model.Gene;

public class DNAUtil {

	
	public static String calculateResults(String filepath) throws Exception {
	
		List<Gene> genes = DNAFileReader.readGFF(filepath);
		
		return null;
	}
	
}
