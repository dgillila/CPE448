package utils;

import java.util.ArrayList;

public class SuffixTreeNode {
	public SuffixTreeEdge incomingEdge;
	public SuffixTreeNode parent;
	public ArrayList<SuffixTreeNode> children;
	public int position = 0;
	
	public SuffixTreeNode()
	{
		this.incomingEdge = null;
		this.parent = null;
		this.children = new ArrayList<SuffixTreeNode>();
	}
	
	public SuffixTreeNode(SuffixTreeEdge edge, SuffixTreeNode parent)
	{
		this.incomingEdge = edge;
		this.parent = parent;
		this.children = new ArrayList<SuffixTreeNode>();
	}
	
	public int compareTo(SuffixTreeNode node, String suffixTreeWord)
	{
		return this.incomingEdge.getLabel(suffixTreeWord).compareTo(node.incomingEdge.getLabel(suffixTreeWord));
	}
}
