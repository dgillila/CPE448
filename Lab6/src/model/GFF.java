package model;

import java.util.ArrayList;
import java.util.List;

public class GFF {

	public List<Gene> genes;
	
	public GFF() {
		genes = new ArrayList<Gene>();
	}
	
	public String writeGFF() {
		StringBuilder rtn = new StringBuilder();
		
		for(Gene g : genes) {
			for(Isoform i : g.getIsoforms()) {
				rtn.append(writeLine(i));
				for(CDS c : i.getCDSRegions()) {
					rtn.append(writeLine(c));
				}
			}
		}
		
		return rtn.toString();
	}
	
	private static String writeLine(Isoform toWrite) {
		StringBuilder line = new StringBuilder();
		
		line.append(toWrite.chromosomeName + "\t");
		line.append("." + "\t");
		line.append(((toWrite instanceof CDS) ? "CDS" : "mRNA") + "\t");
		line.append(toWrite.start + "\t");
		line.append(toWrite.stop + "\t");
		line.append("." + "\t");
		line.append((toWrite.forwardStrand ? "+" : "-") + "\t");
		line.append("." + "\t");
		
		for(String attr : toWrite.attributes.keySet()) {
			line.append(attr + " \"" + toWrite.attributes.get(attr) + "\"; ");
		}
		
		return line.toString() + "\n";
	}
}
