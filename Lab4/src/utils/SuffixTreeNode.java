package utils;

import java.util.ArrayList;

public class SuffixTreeNode {
	public SuffixTreeEdge incomingEdge;
	public SuffixTreeNode parent;
	public ArrayList<SuffixTreeNode> children;
	public int position = 0;
	public char left = '0';
	boolean leftDiverse = false;
	
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
	
	public int compareTo(SuffixTreeNode node)
	{
		return this.incomingEdge.label.compareTo(node.incomingEdge.label);
	}
}
