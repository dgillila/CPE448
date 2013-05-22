package model;

public class Options {

	public double threshholdVal;
	public int start, stop, upstreamSize, minSize, maxSize;
	public String sequence;
	
	public String fastaPath;
	public String gffPath;
	
	public boolean byUpstream;
	public boolean bySize;
	
	public Options() {
		threshholdVal = 10;
		start = 1;
		stop = -1;
		upstreamSize = -1;
		sequence = null;
		minSize = 5;
		maxSize = -1;
		byUpstream = false;
		bySize = true;
	}
	
}
