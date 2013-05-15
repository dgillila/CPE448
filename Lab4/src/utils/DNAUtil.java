package utils;

import java.util.ArrayList;
import java.util.List;

import model.Gene;
import model.Isoform;
import model.Options;

public class DNAUtil {

	
	public static String calculateResults(Options o) throws Exception {
	
		String gffFilepath = o.gffPath;
		String fastaFilepath = o.fastaPath;
		
		List<Gene> genes = null;
		String dnaSequence = null;
		
		if(gffFilepath != null && gffFilepath.length() > 0) {
			genes = DNAFileReader.readGFF(gffFilepath);
		}
		
		if(fastaFilepath != null && fastaFilepath.length() > 0) {
			long start = System.nanoTime();
			dnaSequence = DNAFileReader.readFASTA(fastaFilepath);
			long stop = System.nanoTime();
			
			System.out.println("Time: " + (stop - start));
		}
		
		String results = "";
		
		//ALL OPTION TYPES
		//Options will be sanitized by this point
		//So perform all calculations on the specified values
		if(o.byUpstream) { //Search in front of each mRNA taking into account + and - (+ search in front / - search after upstream size)
			int sizeRange = o.upstreamSize;
			if(o.bySize) { //Search for repeats of specified size
				int minSize = o.minSize;
				int maxSize = o.maxSize < 1 ? dnaSequence.length() : o.maxSize; //Max size the length of the dna sequence... sure				
				
				//TODO Iterate over each mRNA stored in genes and search the
				//upstream area for repeated sequences greater than minSize and
				//less than maxSize
				
				for(Gene gene : genes)
				{
					for(Isoform iso : gene.getIsoforms())
					{
						if(iso.getFeature().equals("mRNA"))
						{
							StringBuffer dnaSubSequence;
							if(iso.isForwardStrand())
							{
								dnaSubSequence = new StringBuffer(dnaSequence.substring(iso.getStart()-1-sizeRange, iso.getStart()-1));
							}
							else
							{
								dnaSubSequence = new StringBuffer(dnaSequence.substring(iso.getStart()+1, iso.getStart()+1+sizeRange)).reverse();
							}
							SuffixTree tree = new SuffixTree(dnaSubSequence.toString(), 1, dnaSubSequence.length());
							ArrayList<String> repeats = SuffixTree.findAllRepeatsOfLength(tree, minSize, maxSize);

							for(int i = 0; i < repeats.size(); i++) 
							{
								results += repeats.get(i) + " ";
							}
						}
					}
				}
				
				
				
			} else { //Search for repeats by specific string
				String searchPattern = o.sequence;
				
				//TODO Iterate over each mRNA stored in genes and search the
				//upstream area for repeats that match the searchPattern string
				
				for(Gene gene : genes)
				{
					for(Isoform iso : gene.getIsoforms())
					{
						if(iso.getFeature().equals("mRNA"))
						{
							StringBuffer dnaSubSequence;
							if(iso.isForwardStrand())
							{
								dnaSubSequence = new StringBuffer(dnaSequence.substring(iso.getStart()-1-sizeRange, iso.getStart()-1));
							}
							else
							{
								dnaSubSequence = new StringBuffer(dnaSequence.substring(iso.getStart()+1, iso.getStart()+1+sizeRange)).reverse();
							}
							SuffixTree tree = new SuffixTree(dnaSubSequence.toString(), 1, dnaSubSequence.length());
							ArrayList<String> repeats = SuffixTree.findRepeats(tree, searchPattern);

							for(int i = 0; i < repeats.size(); i++) {
							    results += repeats.get(i) + " ";
							}
						}
					}
				}
				
				
			}
		} else { //Search in the specified range
			int startPosition = o.start;
			int stopPosition = o.stop < 1 ? dnaSequence.length() : o.stop;
			
			if(o.bySize) { //Search for repeats of specified size
				int minSize = o.minSize;
				int maxSize = o.maxSize < 1 ? dnaSequence.length() : o.maxSize; //Max size the length of the dna sequence... sure
				
				//TODO use a suffix tree and find all repeated sequences
				//in the specified range that are greater than min size 
				//and less than maxSize. Range is specified by startPosition and stopPosition
				String dnaSubSequence = dnaSequence.substring(startPosition-1, stopPosition);
				SuffixTree tree = new SuffixTree(dnaSubSequence, 1, dnaSubSequence.length());

				ArrayList<String> repeats = SuffixTree.findAllRepeatsOfLength(tree, minSize, maxSize);

				for(int i = 0; i < repeats.size(); i++) {
                                    results += repeats.get(i) + " ";
                                }
				
			} else { //Search for repeats by specific string
				String searchPattern = o.sequence;
				
				//use a suffix tree and find all repeated sequences
				//in the specified range that are the same as the searchPattern string
				String dnaSubSequence = dnaSequence.substring(startPosition-1, stopPosition);
				SuffixTree tree = new SuffixTree(dnaSubSequence, 1, dnaSubSequence.length());
				ArrayList<String> repeats = SuffixTree.findRepeats(tree, searchPattern);

				for(int i = 0; i < repeats.size(); i++) {
				    results += repeats.get(i) + " ";
				}
			}
		}
		
		return results;
	}
	
}
