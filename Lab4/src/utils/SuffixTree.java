package utils;

import java.lang.String;
import java.util.ArrayList;

public class SuffixTree {

	public SuffixTreeNode root;
	public String word;
	public int startPos;
	public int endPos;
	
	public SuffixTree(String word, int start, int end)
	{
		this.word = new String(word);
		this.startPos = start;
		this.endPos = end;
		this.constructTree();
	}
	
	public static void main(String[] args)
	{
		SuffixTree tree = new SuffixTree("CCATCAT", 1, 7);
		System.out.println("Done!");
		printTree(tree.root, 0);
		System.out.println();
		
		ArrayList<String> repeats = findRepeats(tree, "CAT");
		for(String repeat : repeats)
		{
			System.out.println(repeat);
		}
	}
	
	public void constructTree()
	{
		this.root = new SuffixTreeNode();
		int i;
		
		for(i = 0; i < this.word.length(); i++)
		{
			this.addSuffix(this.startPos+i, this.endPos+1, this.word.substring(i) + "$", i+1);
		}
		this.addSuffix(this.startPos+i, this.endPos+1, "$", i+1);
	}
	
	public void addSuffix(int start, int end, String suffix, int position)
	{
		StringBuilder tempSuffix = new StringBuilder(suffix);
		SuffixTreeNode node = findPosition(this.root, tempSuffix);
		this.insertSuffix(start, end, node, new String(tempSuffix), position);
	}
	
	//NOTE: Method modifies suffix parameter
	public static SuffixTreeNode findPosition(SuffixTreeNode startNode, StringBuilder suffix)
	{
		int i;
		int charsMatched = -1;
		String label;
		for(i = 0; i < startNode.children.size(); i++)
		{
			label = startNode.children.get(i).incomingEdge.label;
			charsMatched = matchSuffixWithLabel(new String(suffix), label);
			if(charsMatched == label.length())
			{
				//Child Node label is a prefix of the target suffix so continue 
				//traversing tree along this child node path
				return findPosition(startNode.children.get(i), suffix.delete(0, charsMatched));
			}
			else if(charsMatched > 0)
			{
				//Child Node label is a partial prefix of the target suffix so continue 
				//traversing tree along this child node path
				return startNode.children.get(i);
			}
		}
		
		//None of the child nodes has labels matching the target suffix so return current node
		return startNode;
	}
	
	public static int matchSuffixWithLabel(String suffix, String label)
	{
		int i;
		for(i = 0; i < suffix.length() && i < label.length(); i++)
		{
			if(suffix.charAt(i) != label.charAt(i))
			{
				break;
			}
		}
		return i;
	}
	
	public void insertSuffix(int start, int end, SuffixTreeNode node, String suffix, int position)
	{
		if(node.incomingEdge == null || suffix.charAt(0) != node.incomingEdge.label.charAt(0))
		{
			//Case 1: Only add leaf node
			SuffixTreeNode leafNode = new SuffixTreeNode(new SuffixTreeEdge(start, end, suffix), node);
			leafNode.position = position;
			node.children.add(leafNode);
		}
		else
		{
			//Case 2: Split Edge, Create Internal Node
			
			//Construct new label
			int charsMatched = matchSuffixWithLabel(suffix, node.incomingEdge.label);
			String newLabel = new String(suffix.substring(0, charsMatched));
			
			//Remove node from parent's child list
			SuffixTreeNode parent = node.parent;
			this.remove(parent.children, node);
			
			//Create new internal node and Add internal node to parent's child list
			//Start and End Positions for internal nodes are just 0 because multiple suffixes with different positions
			//could be using that internal node as a path so only leaf nodes need start/end positions
			SuffixTreeNode internalNode = new SuffixTreeNode(new SuffixTreeEdge(0, 0, newLabel), parent);
			parent.children.add(internalNode);
			//Add old node (was removed from parent's child list) to internal node's child list
			node.incomingEdge.label = node.incomingEdge.label.substring(charsMatched);
			node.parent = internalNode;
			internalNode.children.add(node);
			//Add new leaf node to finish off rest of suffix
			SuffixTreeNode suffixNode = new SuffixTreeNode(new SuffixTreeEdge(start, end, suffix.substring(charsMatched)), internalNode);
			suffixNode.position = position;
			internalNode.children.add(suffixNode);
		}
	}
	
	public void remove(ArrayList<SuffixTreeNode> list, SuffixTreeNode target)
	{
		int i;
		for(i = 0; i < list.size(); i++)
		{
			if(list.get(i).compareTo(target) == 0)
			{
				list.remove(i);
				break;
			}
		}
	}
	
	//NOTE: This method is not finished yet
	public void maximalRepeatsTraverseTree(SuffixTreeNode node)
	{
		int i;
		boolean diffLeftChars = false;
		ArrayList<Character> diversityLabels = new ArrayList<Character>();
		
		for(i = 0; i < node.children.size(); i++)
		{
			maximalRepeatsTraverseTree(node.children.get(i));
		}
		
		if(node.children.size() == 0)
		{
			//Leaf Node - record left characters
			if(node.position > 1)
			{
				node.left = this.word.charAt(node.position-1);
			}
			else
			{
				//Empty Character - left character for first character of suffix
				node.left = '*';
			}
		}
		else
		{
			//Internal Node
			i = 0;
			for(SuffixTreeNode child : node.children)
			{
				if(!diversityLabels.contains(child.left))
				{
					//Only add new left char labels
					diversityLabels.add(child.left);
				}
				
				if(child.leftDiverse)
				{
					//Internal Node is left diverse if any of the children are
					node.leftDiverse = true;
				}
				else
				{
					//Prune nodes that are not left diverse
					node.children.remove(i);
				}
				i++;
			}
			
			if(diversityLabels.size() > 1)
			{
				//Internal Node's children contain different left characters
				diffLeftChars = true;
			}
			
			if(!node.leftDiverse)
			{
				node.leftDiverse = diffLeftChars;
			}
		}
		
	}
	
	public static ArrayList<String> findRepeats(SuffixTree tree, String target)
	{
		ArrayList<String> repeats = new ArrayList<String>();
		SuffixTreeNode node = findPosition(tree.root, new StringBuilder(target));
		
		if(node.incomingEdge != null)
		{
			//Not Root Node
			getAllStrings(node, repeats, target);
		}
		return repeats;
		
	}
	
	public static void getAllStrings(SuffixTreeNode start, ArrayList<String> repeats, String current)
	{
		if(start.children.size() == 0)
		{
			//Leaf Node
			repeats.add(current);
		}
		for(SuffixTreeNode child : start.children)
		{
			
			getAllStrings(child, repeats, current+child.incomingEdge.label);
		}
		
	}
	
	public static void printTree(SuffixTreeNode start, int depth)
	{
		int i;
		if(start.incomingEdge != null)
		{
			System.out.print("Depth " + depth + ": " + start.incomingEdge.label);
			if(start.children.size() == 0)
			{
				System.out.println(" (Leaf Node) - Start: " + start.incomingEdge.labelStartPos + ", End: " + start.incomingEdge.labelEndPos + " - Iteration Position: " + start.position);
			}
			else
			{
				System.out.println(" (Internal Node) Start: " + start.incomingEdge.labelStartPos + ", End: " + start.incomingEdge.labelEndPos);
			}
		}
		else
		{
			System.out.println("Depth " + depth + ": ROOT");
		}
		for(i = 0; i < start.children.size(); i++)
		{
			printTree(start.children.get(i), depth+1);
		}
	}
}