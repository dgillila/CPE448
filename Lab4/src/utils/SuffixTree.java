package utils;

import java.lang.String;
import java.util.ArrayList;

public class SuffixTree {

	public SuffixTreeNode root;
	public static String word;
	public int startPos;
	public int endPos;
	
	public SuffixTree(String treeWord)
	{
		word = new String(treeWord+"$");
		this.startPos = 1;
		this.endPos = word.length();
		this.constructTree();
	}
	
	public static void main(String[] args)
	{
		String str = new String("AAAAAACTTGAAACCCAAGTCGCGCTCTTGAAACCCAAGTGAACGGTTGAAACCCAAGTTACGCCAAGATTGAAACCCAAGTAA");
		//String str = new String("TCTGAATTTT");
		SuffixTree tree = new SuffixTree(str);
		System.out.println("Done!");
		printTree(tree.root, 0);
		System.out.println();
		
		ArrayList<String> repeats = findRepeats(tree, "TTGAAACCCAAGT");
		//ArrayList<String> repeats = findRepeats(tree, "TTT");

		//ArrayList<String> allStrings = new ArrayList<String>();

		//allStrings = findAllRepeatsOfLength(tree, 2, 4);
		System.out.println("All repeats by sequence: ");
		for(String repeat : repeats)
		{
			System.out.println(repeat);
		}

//		System.out.println("All repeats by size:");
//		for(String s : allStrings)
//		{
//			System.out.println(s);
//		}
		
	}
	
	public void constructTree()
	{
		this.root = new SuffixTreeNode();
		int i;
		
		for(i = this.startPos; i <= this.endPos; i++)
		{
			this.addSuffix(i, this.endPos, word.substring(i-1));
		}
	}
	
	public void addSuffix(int start, int end, String suffix)
	{
		System.out.println("Add Suffix: " + suffix);
		StringBuilder tempSuffix = new StringBuilder(suffix);
		SuffixTreeNode node = findPosition(this.root, tempSuffix);
		int charsRemoved = suffix.length() - tempSuffix.length();
		boolean completeIncomingEdgeMatch = false;
		if(node.incomingEdge != null && charsRemoved >= (node.incomingEdge.labelEndPos - node.incomingEdge.labelStartPos + 1))
		{
			completeIncomingEdgeMatch = true;
		}
		this.insertSuffix(start+charsRemoved, end, node, new String(tempSuffix), start, completeIncomingEdgeMatch);
	}
	
	//NOTE: Method modifies suffix parameter
	public static SuffixTreeNode findPosition(SuffixTreeNode startNode, StringBuilder suffix)
	{
		int i;
		int charsMatched = -1;
		String label;
		
		for(i = 0; i < startNode.children.size(); i++)
		{
			label = startNode.children.get(i).incomingEdge.getLabel(word);
			charsMatched = matchSuffixWithLabel(new String(suffix), label);
			if(charsMatched == label.length())
			{				
				//Child Node label is a prefix of the target suffix so continue 
				//traversing tree along this child node path
				return findPosition(startNode.children.get(i), suffix.delete(0, charsMatched));
			}
			else if(charsMatched > 0)
			{
				//Child Node label is a partial prefix of the target suffix so return child node
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
	
	public boolean checkForChildPrefixMatches(SuffixTreeNode node, String suffix)
	{
		for(SuffixTreeNode child : node.children)
		{
			if(child.incomingEdge.getLabel(word).charAt(0) == suffix.charAt(0))
			{
				//Found partial prefix match in child
				return true;
			}
		}
		//No prefix matches in children
		return false;
	}
	
	public void insertSuffix(int start, int end, SuffixTreeNode node, String suffix, int position, boolean completeIncomingEdgeMatch)
	{
		if(node.incomingEdge == null || completeIncomingEdgeMatch || suffix.charAt(0) != node.incomingEdge.getLabel(word).charAt(0)) 
		{
			//Case 1: Only add leaf node
			SuffixTreeNode leafNode = new SuffixTreeNode(new SuffixTreeEdge(start, end), node);
			leafNode.position = position;
			node.children.add(leafNode);
		}
		else
		{
			//Case 2: Split Edge, Create Internal Node
			
			//Construct new label
			int charsMatched = matchSuffixWithLabel(suffix, node.incomingEdge.getLabel(word));
			
			//Remove node from parent's child list
			SuffixTreeNode parent = node.parent;
			this.remove(parent.children, node);
			
			//Create new internal node and Add internal node to parent's child list
			int internalNodeStart = node.incomingEdge.labelStartPos;
			SuffixTreeNode internalNode = new SuffixTreeNode(new SuffixTreeEdge(internalNodeStart, internalNodeStart+charsMatched-1), parent);
			parent.children.add(internalNode);
			//Add old node (was removed from parent's child list) to internal node's child list
			node.incomingEdge.labelStartPos = internalNodeStart+charsMatched;
			node.parent = internalNode;
			internalNode.children.add(node);
			//Add new leaf node to finish off rest of suffix
			SuffixTreeNode suffixNode = new SuffixTreeNode(new SuffixTreeEdge(start+charsMatched, end), internalNode);
			suffixNode.position = position;
			internalNode.children.add(suffixNode);
		}
	}
	
	public void remove(ArrayList<SuffixTreeNode> list, SuffixTreeNode target)
	{
		int i;
		for(i = 0; i < list.size(); i++)
		{
			if(list.get(i).compareTo(target, word) == 0)
			{
				list.remove(i);
				break;
			}
		}
	}
	
	public static ArrayList<String> findRepeats(SuffixTree tree, String target)
	{
		ArrayList<String> repeats = new ArrayList<String>();
		getRepeatsUsingTarget(tree.root, repeats, target, "");
		return repeats;
		
	}

     public static ArrayList<String> findAllRepeatsOfLength(SuffixTree tree, int min, int max)
     {
     	ArrayList<String> repeats = new ArrayList<String>();
     	ArrayList<SuffixTreeNode> internalNodes = new ArrayList<SuffixTreeNode>();
     	ArrayList<String> fullInternalNodeLabels = new ArrayList<String>();
     	
     	getAllInternalNodesOfLength(tree.root, internalNodes, fullInternalNodeLabels, "", min, max);
     	
     	int i;
     	for(i = 0; i < internalNodes.size() && i < fullInternalNodeLabels.size(); i++)
     	{
     		getLeafStringsFromNode(internalNodes.get(i), fullInternalNodeLabels.get(i), repeats);
     	}

		return repeats;
     }			     
	
	public static void getRepeatsUsingTarget(SuffixTreeNode start, ArrayList<String> repeats, String target, String current)
	{
		if(start.children.size() == 0 && current.startsWith(target))
		{
			//Leaf Node
			repeats.add(current);
		}
		else if(current.length() == 0 || target.startsWith(current))
		{
			//Root Node or Path label is prefix of target string
			for(SuffixTreeNode child : start.children)
			{
				
				getRepeatsUsingTarget(child, repeats, target, current+child.incomingEdge.getLabel(word));
			}
		}
		
	}
	
	public static void getAllInternalNodesOfLength(SuffixTreeNode start, ArrayList<SuffixTreeNode> nodes, ArrayList<String> nodeLabels, String current, int min, int max)
	{
		if(current.length() >= min && current.length() <= max && start.children.size() > 0)
		{
			nodes.add(start);
			nodeLabels.add(current);
		}
		else if(current.length() <= max)
		{
			for(SuffixTreeNode child : start.children)
			{
				getAllInternalNodesOfLength(child, nodes, nodeLabels, current+child.incomingEdge.getLabel(word), min, max);
			}
		}
	}
	
	public static void getLeafStringsFromNode(SuffixTreeNode start, String currentLabel, ArrayList<String> repeats)
	{
		if(start.children.size() == 0)
		{
			repeats.add(currentLabel);
		}
		else
		{
			for(SuffixTreeNode child : start.children)
			{
				getLeafStringsFromNode(child, currentLabel+child.incomingEdge.getLabel(word), repeats);
			}
		}
	}
	
	public static void printTree(SuffixTreeNode start, int depth)
	{
		int i;
		if(start.incomingEdge != null)
		{
			System.out.print("Depth " + depth + ": " + start.incomingEdge.getLabel(word));
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