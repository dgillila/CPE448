package utils;

public class SuffixTreeEdge {
	public int labelStartPos;
	public int labelEndPos;
	public String label;
	
	public SuffixTreeEdge(int start, int end, String label)
	{
		this.labelStartPos = start;
		this.labelEndPos = end;
		this.label = new String(label);
	}

}
