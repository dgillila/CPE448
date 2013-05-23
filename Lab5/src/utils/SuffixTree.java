package utils;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class SuffixTree {

	public SuffixTreeNode root;
	public static String word;
	public int startPos;
	public int endPos;
	
	
	public SuffixTree(String original, String rCompliment) {
		//TODO Create the generalized suffix tree
		
	}
	
	public List<Integer> findPalindromes(boolean allowUGPairing, int minPalindromeSize, int maxPalindromeSize, int minLoopSize, int maxLoopSize) {
		//TODO find palindrome locations
		//minLoopSize and maxLoopSize are what we know as gap size
		
		List<Integer> locations = new ArrayList<Integer>();
		
		
		
		
		
		return locations;
	}
	
	public SuffixTree(String treeWord)
	{
		word = new String(treeWord+"$");
		this.startPos = 1;
		this.endPos = word.length();
		this.constructTree();
	}
	
	public static void main(String[] args)
	{
		//String str = new String("AATATTTATTTGTTACAATGAGTGAGTGTCTTTACTTTACAATATACCCTAGAGAAATGGGTTACAGGCTATATAGACTCAATACTCAGGTAAAGAGAGCGTGAGTGAGATAATGATATGAAATTAGCAATGCTATTATTTCTACACTAGCGCCACTTAGCGGCACTTACTTTTATTGCTCACGAACGACATCCTGTGGTACGCCTTGAAATCTTATTTTATTAAAAACCTTTACGGTAAAATAAGTGTGATTTTCAAATTTCATATTCTTTAAAATCGAAGTAATTTTCCATAACGTTTGTGTGCCCGTGCTGGAGGTTTGGGGGCGTGGTAACCTTCTAAAACAAACTTGTGCTGCACAAGTGTCACTAGAGTTTCCATGTTTTATGTAGCAACATGTTAAATGTTACACATACACGGCCTTAAAAACATTACTGAACAATAATCTGTTTGGTGGAATTCAAAAACAGCACATTATTAACATTGAGTTCCTATCAAAAACAGCACATTATTAACATTGAGTTCCTATTTCTCGCATCGTAATCAAAAAAACGTTGTCATGATTCATTAATTATTTACTAAAGATGTCATTTTGTAACACAGTGCTATTAATAAAGCGTTCTTACATATTTGTTCGAACATGTGAAATCCACAAATCTTACCATATTTATATTTTGGTTATCATATTCGTTTGTTTGTGACCTTTTTGATTTGCTCTTTTTTGAGGGTAATTTATTTCCAGAAGATATTTTTGAACTATTTGGATTCTGAGTGTCCATGTTAATTTGTTCGTCTTCCTGCTAAATTTTAAAGTGATGACTTAAATAGGATTAAGTCAGATTAAATAAATTAAATAAGATTAAATACATATAATTATTTTATTTTAATTAAAAAGAATTTAATACGCTCTATCCTATAGTGCAAATTTAAAATTTTAAGGTTTTGACTTAAATAAGTTAGCATTACTAAAATAAAGTGCTTAACAAAAACACTTGACAAGGTAAAAGTCAAGGGTCAATTGCACCCTCAACGAATAAAACTTTCTAATATAATCTTTTGTGACGAAAATTAAATTGCACTTGATTTTATAAATACTTGAGTGATACATGAATGTAAAGATAGCGCTAAGAATGCAGGGATTGCATACCAAAACAAAATAAGTACATGGCTAACGTGAAATAACATTTAAATAATAAAAAAAAATGTAGTTTTACCACTCTTAAAAATCAATAGTTTAGCAGAAAAACAGATTAAACATACCATATACCAACGCTCAAGACTTTTGAACACTGGGGTTAGAAAGTTTTGCTATATGCCATTAAAGGCATTTTGACATGTATTAGGAGAAAAACAGATTAAAGAAACCATATTCAAACGATAATAACTTCTGAACACTTGAGGTTAGAAACTTTTGCTATATGCCATTGAAGGCATTTTGACATATATTTTGTCTGAGTACAATACAAAAAAAAACTAATACAAAATACGTAATTTTATTTGTTTAATGTAATATTTAAATATACTACCCGAAATAAAAATACAATCCACATTAGGGGGATACTATCAAAAAGTGTTTGTATTTGCTTAACAGGGGTTCAAAAAACTATACTCCCATCTTTTGTTCAAATTTTTATTATTTAGAAGAACACGTAGCACTTAAGGAATATACACTTTTGGGAAAAATTGAAACTTTATTTATAATACCTATGTCTGAACTCTAGCTAGGATATTTATATAATAAAAAAGCTTTTCTACGAGTTCATGCTTTTTAGTGTATATATGAATACACACTGAACAATAGCACACTCTTTGGCATCGTATTGTACCTTATACATAATTTTAAATGATCCACAAAATATATAAGAGTTTGCATCCAAGTATGTCATCGATGATGGTTTCACAAATTAATAATGAGTATATTAACTAATTATACTGATGCTTAAAATATAGGTTGTTAAAAGCAGACCTATCTGGATGTTTTATCATTAGTAATTAACGTTCGGAAGTTGAAGTCCGACTGTTTCTTAGCGTTGGCAGATTCGGTACATGTAGATACAATGTTCAAAATTGAGTTAATTTTAAATGAGTAAAAGCTTTCTTCCAAAAATATTTAAAAGCTTTTAAAGCAAGTAGAATGCACATTTGGAATTCAGACCTGTATAAAAAGAAATAAACCTGCAAGACATTCTATTTCTAAGTAAAGCAAGAAAGAATGATATAGTCAGTTCCTCTACTATCAGATACTGGGTTCTTAGCTAGAGTAAGTGCGACGGAGATAGAATATATAAAAACAAATAGGAAAAGACATTTCGAGCGCTAACCAGCACAACCTGCTGCCGGCTACATTATTTTAATTTGTAGTCGTTAGGTATAGAACAAATAATAAAGTAAGATATCTTCTGCGAACCAAAGTTGATATACTTTTTAAGTATTTATATATTTGGAAAATTATGTATACTGAATAATAATACTTTATGATTAAAACAACGTAACTAAGACTAATTTTCTATTATTTTTAAATAATTTGTATACTGCTTTAATAGCAACAATAGGATTTCGTCTCCGATTTTTAAATTAAAACCGAGTAACCTCATCAGGCAGATCTCCATAATGAGCTTTAAAAATGGGAGGCCGCAATGCTGCCGGCTATTCTAGCCCCACATTTATCAAAATAAATAGTGGGAGGAAGTACTCGTTATCTAACGCATAAGTTGGTGTAAATGTTTTTTTACATAATAATTTAGCCATTAAGTCCCATTAATCAATATCAAAAGCCGTTAAAATGTCGAATATGTTTTACCTAACAAAGATGCAAAACTGGATACAAGTGATCAGTACTGGTATCAATAACATGTTCGAGGAAGCCAATACTTTGCGCAAATTGTTAAATGTCGTTTCAAAGGAAGGCGATAACTTCAAATTCGGCCAGATAAAATGTTACGGATGTTAAGGGTGTCGCAGAAATAAAAGTATCCGAGACAATGAGACGGGTAGTTTTCTAGACCTAATTCCACGCTAAGTCCGAGAAATGGCCAGCTCAAAAGTGCATAAACGTGAGCTGTGTACATGGCCTCCCAAAATTCAGCAAATCTGATTGCAGAAAACCAATATTTGGACTGCGTAAAAAAGTGATAACGTAATATATTTTTTTGCTATTGTATTGTGTTGTGAGTCGTAGATACTTCGTGGCTACTTTTTATTTTTGGTTTTTATTTATCATTACTTTATTTACAATATTTATACTTAGTCATGTTAATTTAATTATACGATCATTTTCTTTTCATTTGATTTCTTCCTTTCCGAAATTTCCTTGATCGGAAAGGTTTTCAGAATTATTGCAAAATTATTATTCTTTAGAGAATAGGCAGCGTCTCATTAGGCAGGGATTTAGGCTTGGAAGCTATCGATGGTCTAACGAGATATCCAATTTTTCCTCTCTAAGCCAACATCGGTTTTATTTGTCGCTAATGCTACATTTAGCATTTTTCACATCTCTCAAATTAGTACAAACCAAGGCTTTGTTGCAGATTTTTGGGTAGGAAAAATTCAA");
		//str = new String("TCTGAATTTT");
		String str = new String("ATAT");
		SuffixTree tree = new SuffixTree(str);
		tree.insertNewSuffix("TATT");
		System.out.println("Done!");
		printTree(tree.root, 0);
		System.out.println();
		
		ArrayList<String> repeats = findRepeats(tree, "TTT");

		ArrayList<String> allStrings = new ArrayList<String>();

		allStrings = findAllRepeatsOfLength(tree, 2, 4);
		
		System.out.println("All repeats of target sequence:");
		for(String repeat : repeats)
		{
			System.out.println(repeat);
		}

		System.out.println("All repeats of certain length:");
		for(String s : allStrings)
		{
			System.out.println(s);
		}
		
	}
	
	public void constructTree()
	{
		
		//Constructing tree from first string
		this.root = new SuffixTreeNode();
		
		int i;
		for(i = this.startPos; i <= this.endPos; i++)
		{
			this.addSuffix(i, this.endPos, word.substring(i-1));
		}
	}
	
	//NOTE: Assumes that tree has been initialized already
	public void insertNewSuffix(String newSuffix)
	{
		int oldStrLength = word.length();
		word = new String(word + newSuffix + "$");
		this.startPos = oldStrLength+1;
		this.endPos = word.length();
		
		int i;
		for(i = this.startPos; i <= this.endPos; i++)
		{
			System.out.println("Loop: " + word.substring(i-1));
			this.addSuffix(i, this.endPos, word.substring(i-1));
		}
	}
	
	public void addSuffix(int start, int end, String suffix)
	{
		StringBuilder tempSuffix = new StringBuilder(suffix);
		SuffixTreeNode node = findPosition(this.root, tempSuffix);
		if(tempSuffix.length() > 0)
		{
			int charsRemoved = suffix.length() - tempSuffix.length();
			this.insertSuffix(start+charsRemoved, end, node, new String(tempSuffix), start);
		}
		else
		{
			//Length of tempSuffix is 0 it means that a complete match in tree was found
			//so there is nothing to insert
			node.positions.add(start);
		}
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
		if(node.incomingEdge == null || suffix.startsWith(node.incomingEdge.getLabel(word)) || 
				suffix.charAt(0) != node.incomingEdge.getLabel(word).charAt(0))
		{
			//Case 1: Only add leaf node
			SuffixTreeNode leafNode = new SuffixTreeNode(new SuffixTreeEdge(start, end), node);
			leafNode.positions.add(position);
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
			suffixNode.positions.add(position);
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
	
	//NOTE: This is naive implementation of lowest common ancestor algorithm
	public static SuffixTreeNode findLowestCommonAncestor(SuffixTreeNode node1, SuffixTreeNode node2)
	{
		SuffixTreeNode t1 = node1;
		SuffixTreeNode t2 = node2;
		
		while(t1.compareTo(t2, word) != 0)
		{
			if(t1.incomingEdge == null || t2.incomingEdge == null)
			{
				//One of the nodes hit the root
				break;
			}
			else
			{
				t1 = t1.parent;
				t2 = t2.parent;
			}
		}
		
		if(t1.incomingEdge == null)
		{
			//Node 1 hit the root so that is lca
			return t1;
		}
		else
		{
			//Either Node 2 hit the root or the nodes are equal so it is lca
			return t2;
		}
	}
	
	//NOTE: Returns null in target if s1 is not found
	public static void findNode(SuffixTreeNode start, String s1, String current, SuffixTreeNode target)
	{
		if(current.equals(s1))
		{
			target = start;
		}
		else if(start.children.size() == 0)
		{
			target = null;
		}
		else
		{
			if(current.length() < s1.length() && s1.startsWith(current))
			{
				for(SuffixTreeNode child : start.children)
				{
					findNode(child, s1, current+child.incomingEdge.getLabel(word), target);
				}
			}
		}
	}
	
	//NOTE: returns null if strings s1 or s2 were not found in tree
	public static SuffixTreeNode getLongestCommonExtension(SuffixTreeNode start, String s1, String s2)
	{
		SuffixTreeNode n1 = new SuffixTreeNode();
		SuffixTreeNode n2 = new SuffixTreeNode();
		
		SuffixTree.findNode(start, s1, "", n1);
		SuffixTree.findNode(start, s2, "", n2);
		
		if(n1 != null && n2 != null)
		{
			return SuffixTree.findLowestCommonAncestor(n1, n2);
		}
		else
		{
			return null;
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
				System.out.print(" (Leaf Node) - Start: " + start.incomingEdge.labelStartPos + ", End: " + start.incomingEdge.labelEndPos + " - Iteration Positions: ");
				int j;
				for(j = 0; j < start.positions.size() - 1; j++)
				{
					System.out.print(start.positions.get(j) + ", ");
				}
				System.out.println(start.positions.get(j));
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