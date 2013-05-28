package utils;

import java.util.List;

import model.Options;
import model.PalindromeResult;

public class DNAUtil {

	public static String calculateResults(Options o) throws Exception {

		String fastaFilepath = o.fastaPath;

		String dnaSequence = null;
		String reverseCompliment = null;

		if (fastaFilepath != null && fastaFilepath.length() > 0) {
			long start = System.nanoTime();
			dnaSequence = DNAFileReader.readFASTA(fastaFilepath);
			long stop = System.nanoTime();

			System.out.println("Time: " + (stop - start));
		}

		reverseCompliment = doReverseCompliment(dnaSequence);

		SuffixTree tree = new SuffixTree(dnaSequence, reverseCompliment);
		
		List<PalindromeResult> palindromePositions = tree.findPalindromes(o.allowUGPairs, o.minPalindromeSize, o.maxPalindromeSize, o.minLoopSize, o.maxLoopSize);
		
		StringBuilder results = new StringBuilder();
		results.append("Postion :     Sequence\n\n");
		
		for(PalindromeResult res : palindromePositions) {
			results.append(res.leftWingStart + " : " 
							+ dnaSequence.substring(res.leftWingStart-1, res.leftWingEnd) 
							+ "  " + dnaSequence.substring(res.leftWingEnd, res.rightWingStart-1)
							+ "  " + dnaSequence.substring(res.rightWingStart-1, res.rightWingEnd) + "\n");
		}

		return results.toString();
	}

	public static String doReverseCompliment(String sequence) {

		StringBuilder complimentBuilder = new StringBuilder();

		for (int i = sequence.length()-1; i >= 0; i--) {
			complimentBuilder.append(getCompliment(sequence.charAt(i)));
		}

		return complimentBuilder.toString();
	}

	public static char getCompliment(char c) {
		switch (c) {
		case 'U':
			return 'A';
		case 'A':
			return 'U';
		case 'G':
			return 'C';
		case 'C':
			return 'G';
		}
		return 'N';
	}

}
