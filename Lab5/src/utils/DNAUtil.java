package utils;

import java.util.List;

import model.Options;

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
		
		List<Integer> palindromePositions = tree.findPalindromes(o.allowUGPairs, o.minPalindromeSize, o.maxPalindromeSize, o.minLoopSize, o.maxLoopSize);
		
		String results = "";

		// DO CALCULATIONS HERE

		return dnaSequence + "\n" + reverseCompliment;
	}

	private static String doReverseCompliment(String sequence) {

		StringBuilder complimentBuilder = new StringBuilder();

		for (int i = sequence.length()-1; i >= 0; i--) {
			complimentBuilder.append(getCompliment(sequence.charAt(i)));
		}

		return complimentBuilder.toString();
	}

	private static char getCompliment(char c) {
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
