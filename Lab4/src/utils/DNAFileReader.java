package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import model.CDS;
import model.Gene;
import model.Isoform;

public class DNAFileReader {

	public static List<Gene> readGFF(String filepath) throws Exception {
		File f = new File(filepath);
		FileReader in = new FileReader(f);
		BufferedReader reader = new BufferedReader(in);

		List<Gene> rtn = new ArrayList<Gene>();

		String line;
		do {
			line = reader.readLine();
			if (line != null) {
				// Break the line apart by the tabs
				String[] tokens = line.split("\t");

				// Is this an mRNA or a CDS?
				if (tokens[2].equals("mRNA")) {
					// mRNA
					Isoform g = new Isoform();
					String geneName = null;

					// Set first attributes
					g.setChromosomeName(tokens[0]);
					g.setSource(tokens[1]);
					g.setFeature(tokens[2]);
					g.setStart(Integer.parseInt(tokens[3]));
					g.setStop(Integer.parseInt(tokens[4]));
					g.setScore(tokens[5].equals(".") ? 0 : Double
							.parseDouble(tokens[5]));
					g.setForwardStrand(tokens[6].equals("+") ? true : false);
					g.setFrame(tokens[7].equals(".") ? 0 : Integer
							.parseInt(tokens[7]));

					String[] attributes = tokens[8].split(";");
					// Save all attributes
					for (String s : attributes) {
						s = s.trim();
						String[] pair = s.split(" ");
						String key = pair[0];
						String val = pair[1];

						if (key.equals("transcript_id")) {
							g.setName(val.replace("\"", ""));
							g.addAttribute(key, val.replace("\"", ""));
						} else if (key.equals("gene_id")) {
							g.addAttribute(key, val.replace("\"", ""));
							geneName = val.replace("\"", "");
						} else {
							g.addAttribute(key, val.replace("\"", ""));
						}
					}

					// Update length with + or - 3
					if (g.isForwardStrand()) {
						g.setStop(g.getStop() + 3);
					} else {
						g.setStart(g.getStart() - 3);
					}

					// Add the completed gene to the list of Genes to return
					if (rtn.contains(geneName)) {
						rtn.get(rtn.indexOf(geneName)).addIsoform(g);
					} else {
						Gene newGene = new Gene();
						newGene.setName(geneName);
						newGene.addIsoform(g);
						rtn.add(newGene);
					}

				} else if (tokens[2].equals("CDS")) {
					// CDS
					CDS c = new CDS();
					String geneName = null;

					c.setChromosomeName(tokens[0]);
					c.setSource(tokens[1]);
					c.setFeature(tokens[2]);
					c.setStart(Integer.parseInt(tokens[3]));
					c.setStop(Integer.parseInt(tokens[4]));
					c.setScore(tokens[5].equals(".") ? 0 : Double
							.parseDouble(tokens[5]));
					c.setForwardStrand(tokens[6].equals("+") ? true : false);
					c.setFrame(tokens[7].equals(".") ? 0 : Integer
							.parseInt(tokens[7]));

					String[] attributes = tokens[8].split(";");
					// Save all attributes
					for (String s : attributes) {
						s = s.trim();
						String[] pair = s.split(" ");
						String key = pair[0];
						String val = pair[1];

						if (key.equals("transcript_id")) {
							c.setName(val.replace("\"", ""));
							c.addAttribute(key, val.replace("\"", ""));
						} else if (key.equals("gene_id")) {
							c.addAttribute(key, val.replace("\"", ""));
							geneName = val.replace("\"", "");
						} else {
							c.addAttribute(key, val.replace("\"", ""));
						}
					}
					
					if(rtn.contains(geneName)) {
						Isoform i = rtn.get(rtn.indexOf(geneName)).findIsoform(c.getName());
						if(i != null) {
							i.addCDS(c);
						} else {
							in.close();
							reader.close();
							throw new Exception("CDS found without an mRNA");
						}
					} else {
						in.close();
						reader.close();
						throw new Exception("CDS found without an mRNA");
					}

				}
			}

		} while (line != null);

		reader.close();

		return null;
	}

	public static String readFASTA(String filepath) throws Exception {

		return null;
	}

}
