package utils;

public class AttributeInfo 
{
	public String id;
	public int count;
	public int totalSize;
	
	public AttributeInfo(String id, int totalSize)
	{
		this.id = new String(id);
		this.count = 1;
		this.totalSize = totalSize;
	}
	
	public void add(int totalSizeIncrement)
	{
		this.count++;
		this.totalSize += totalSizeIncrement;
	}
}

