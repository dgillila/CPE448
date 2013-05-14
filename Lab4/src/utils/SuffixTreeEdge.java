package utils;

public class SuffixTreeEdge {
	//Only leaf nodes use start/end positions
	//Start/end positions represent string that is created if you follow
	//path from root to that particular leaf node
	public int labelStartPos; //Inclusive
	public int labelEndPos; //Inclusive
	public String label;
	
	public SuffixTreeEdge(int start, int end, String label)
	{
		this.labelStartPos = start;
		this.labelEndPos = end;
		this.label = new String(label);
	}

}
