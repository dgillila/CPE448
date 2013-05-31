package model;

public class PalindromeResult {
	
	public int leftWingStart;
	public int leftWingEnd;
	public int rightWingStart;
	public int rightWingEnd;
	
	public PalindromeResult() {
		
	}
	
	public PalindromeResult(int leftStart, int leftEnd, int rightStart, int rightEnd) {
		this.leftWingStart = leftStart;
		this.leftWingEnd = leftEnd;
		this.rightWingStart = rightStart;
		this.rightWingEnd = rightEnd;
	}

}
