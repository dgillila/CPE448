package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Gene;

public class DNAUtil {
	
	public double avgCDSSpan = 0;
	public Map<String, Double> averageCDSSizes = new HashMap<String, Double>();
	public Map<String, Double> averageExonSizes = new HashMap<String, Double>();
	public Map<String, Double> averageIntronSizes = new HashMap<String, Double>();
	public int totalNucleotides = 0;
	public int numGenes = 0;
	
	//Static methods again works for me

	public static String calculateResults(String filepath) throws Exception {
		
		//Name of the gene to all of the attributes of it
		Map<String, List<Gene>> genes = new HashMap<String, List<Gene>>();
		
		readFile(filepath, genes);
		
		return displayInfo(genes);
	}

	private static String displayInfo(Map<String, List<Gene>> genes) {
		String rtn = "";

		for(String name : genes.keySet()) {
			rtn += (name + " " + genes.get(name).size() + "\n");
		}
		
		return rtn;
	}
	
	private static void readFile(String filepath, Map<String, List<Gene>>genes) throws Exception {
		//Set up the file reader
		File f = new File(filepath);
		FileReader in = new FileReader(f);
		BufferedReader reader = new BufferedReader(in);
		
		
		String line = null;
		do {
			line = reader.readLine();
			
			if(line != null) {
				//Break the line apart by the tabs
				String[] tokens = line.split("\t");
				
				//Create a new Gene to save the parts
				Gene g = new Gene();
				
				//Set all the normal stuff
				g.setChromosomeName(tokens[0]);
				g.setSource(tokens[1]);
				g.setFeature(tokens[2]);
				g.setStart(Integer.parseInt(tokens[3]));
				g.setStop(Integer.parseInt(tokens[4]));
				g.setScore(tokens[5].equals(".") ? 0 : Double.parseDouble(tokens[5]));
				g.setForwardStrand(tokens[6].equals("+") ? true : false);
				g.setFrame(tokens[7].equals(".") ? 0 : Integer.parseInt(tokens[7]));
				
				String[] attributes = tokens[8].split(";");
				
				//Save all attributes
				for(String s : attributes) {
					String[] pair = s.split(" ");
					String key = pair[0];
					String val = pair[1];
					
					if(key.equals("gene_id")){
						g.setName(val.replace("\"", ""));
					} else {
						g.addAttribute(key, val.replace("\"", ""));
					}
				}
				
				//The gene is finished reading save it to the gene map
				if(genes.get(g.getName()) == null) {
					List<Gene> temp = new ArrayList<Gene>();
					temp.add(g);
					genes.put(g.getName(), temp);
				} else {
					genes.get(g.getName()).add(g);
				}
				
			}
		} while(line != null);
		
		//All done
		reader.close();
	}
	
	
	private void calculateGeneContentAndDensity(Map<String, List<Gene>> genes)
	{
		List<Gene> gList;
		int mRNATotalSize = 0;
		int mRNACount = 0;
		Map<String, AttributeInfo> isoforms = new HashMap<String, AttributeInfo>();
		Map<String, AttributeInfo> geneInfos = new HashMap<String, AttributeInfo>();
		int totalCDS = 0;
		
		for(String name : genes.keySet()) {
			 gList = genes.get(name);
			 for(Gene g : gList)
			 {
				 //Average CDS Span
				 if(g.getFeature().equals("mRNA"))
				 {
					 mRNACount++;
					 mRNATotalSize += g.getStop() - g.getStart();
				 }
				 else if(g.getFeature().equals("CDS"))
				 {
					 totalCDS++;
					//Average CDS size for each isoform
					 String transcriptID = g.getAttributes().get("transcript_id");
					 if(isoforms.containsKey(transcriptID))
					 {
						 isoforms.get(transcriptID).add(g.getStop() - g.getStart());
					 }
					 else
					 {
						 isoforms.put(transcriptID, new AttributeInfo(transcriptID, g.getStop() - g.getStart()));
					 }
					 
					 //Average Exon Size for each gene that is a CDS
					 String geneID = g.getAttributes().get("gene_id");
					 if(geneInfos.containsKey(geneID))
					 {
						 geneInfos.get(geneID).add(g.getStop() - g.getStart());
					 }
					 else
					 {
						 geneInfos.put(geneID, new AttributeInfo(geneID, g.getStop() - g.getStart()));
					 }
				 }
				 
				 //Finding highest Nucleotide number
				 this.totalNucleotides = (g.getStop() > this.totalNucleotides) ? g.getStop() : this.totalNucleotides;
			 }
		}
		
		//Average CDS Span
		this.avgCDSSpan = (double)mRNATotalSize/mRNACount;
		//Average CDS size for each isoform
		for(String name : isoforms.keySet())
		{
			AttributeInfo iso = isoforms.get(name);
			//Is it divided by number of distinct isoforms for all genes or just total number of isoforms???s
			double avgCDS = (double)iso.totalSize/isoforms.keySet().size();
			this.averageCDSSizes.put(iso.id, avgCDS);
			//Average Intron Size for each isoform - not sure if this is right???
			this.averageIntronSizes.put(iso.id,  (avgCDS - this.avgCDSSpan)/totalCDS);
		}
		//Average Exon Size for each gene
		for(String name : geneInfos.keySet())
		{
			AttributeInfo geneInfo = geneInfos.get(name);
			this.averageExonSizes.put(geneInfo.id, (double)geneInfo.totalSize/geneInfo.count);
		}
		//Number of genes
		this.numGenes = geneInfos.keySet().size();
	}
	
}
