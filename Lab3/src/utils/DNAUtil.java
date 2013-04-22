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
	
}
