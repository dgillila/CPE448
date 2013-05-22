package model;

public class Options {

	public int minLoopSize, maxLoopSize;
	public int minPalindromeSize, maxPalindromeSize;
	public int maxGapSize;
	public int branchCheckSize;
	public boolean allowUGPairs;
	
	public String fastaPath;
	
	public Options() {
		allowUGPairs = false;
		maxGapSize = 3;
		minPalindromeSize = 2;
		maxPalindromeSize = Integer.MAX_VALUE;
		minLoopSize = 3;
		maxLoopSize = 60;
		branchCheckSize = 60;
	}
	
}
