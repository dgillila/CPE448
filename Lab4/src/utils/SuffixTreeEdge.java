package utils;

public class SuffixTreeEdge {
	public int labelStartPos = 0; //Inclusive
	public int labelEndPos = 0; //Inclusive
	
	public SuffixTreeEdge(int start, int end)
	{
		this.labelStartPos = start;
		this.labelEndPos = end;
	}
	
	public String getLabel(String word)
	{
		return word.substring(this.labelStartPos-1, this.labelEndPos);
	}

}
