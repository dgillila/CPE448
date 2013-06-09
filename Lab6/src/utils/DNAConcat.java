package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CDS;
import model.GFF;
import model.Gene;
import model.Isoform;
import net.lingala.zip4j.core.ZipFile;

public class DNAConcat {
	
	private static String FASTA_DIR = "~tempFASTA";
	private static String GFF_DIR = "~tempGFF";
	
	private static String SUPER_CONTIG = "supercontig";
	private static String SUPER_GFF = "supergff";
	
	private static Map<String, String> protienMap = new HashMap<String, String>();
	
	/**
	 * Does the main processing of the files
	 * 
	 * @param fastaZip
	 * @param gffZip
	 * @return
	 */
	public static String processFiles(String fastaZip, String gffZip) throws Exception {
		
		File fastaDir = createTempDir(FASTA_DIR, fastaZip);
		File gffDir = createTempDir(GFF_DIR, gffZip);
		
		File resultDir = new File("results");
		resultDir.mkdir();
		
		int supernumber = 1;
		
		StringBuilder currentFASTA = new StringBuilder();
//		String currentFASTA = "";
		GFF currentGFF = new GFF();
		
		File fastaFiles[] = fastaDir.listFiles();
		File gffFiles[] = gffDir.listFiles();
		
		sortFiles(fastaFiles);
		sortFiles(gffFiles);
		
		if(fastaFiles.length > 0) {
			currentFASTA.append(DNAFileReader.readFASTA(fastaFiles[0].getAbsolutePath()));
			currentGFF.genes = DNAFileReader.readGFF(gffFiles[0].getAbsolutePath());
		} else {
			return "Specified directory is empty";
		}
		
		for(int i = 1; i < fastaFiles.length - 1; i++) {
			File f = fastaFiles[i];
			String toAppend = DNAFileReader.readFASTA(f.getAbsolutePath());
			File g = gffFiles[i];
			List<Gene> toAppendGFF = DNAFileReader.readGFF(g.getAbsolutePath());
			
			int contigLength = currentFASTA.length();
			
			int overlap = mergeStrings(new StringBuilder(currentFASTA), new StringBuilder(toAppend));
//			System.out.println("Overlap: " + overlap);

			if(overlap > 0) {
				//play with the GFF's and fix them up
				for(Gene gene : toAppendGFF) {
					for(Isoform isoform : gene.getIsoforms()) {
						isoform.setStart(contigLength + isoform.getStart() - overlap);
						isoform.setStop(contigLength + isoform.getStop() - overlap);
						for(CDS cds : isoform.getCDSRegions()) {
							cds.setStart(contigLength + cds.getStart() - overlap);
							cds.setStop(contigLength + cds.getStop() - overlap);
						}
					}
					
					if(!geneMatch(gene, currentGFF.genes)) {
						currentGFF.genes.add(gene);
					} else {
//						System.out.println("GENE MATCH");
					}
				}
			}
			
			if(overlap <= 0) {
				//Write the supercontig
				File fastaToWrite = new File("results/" + SUPER_CONTIG + "" + supernumber + ".fasta");
				fastaToWrite.createNewFile();
				FileWriter fw = new FileWriter(fastaToWrite.getAbsoluteFile());
				BufferedWriter writer = new BufferedWriter(fw);

				writer.write(currentFASTA.toString());
				writer.close();
				
				for(int frameNum = 1; frameNum <= 6; frameNum++) {
					File frameToWrite = new File("results/" + SUPER_CONTIG + "" + supernumber + "_frame" + frameNum + ".protein");
					frameToWrite.createNewFile();
					fw = new FileWriter(frameToWrite.getAbsoluteFile());
					writer = new BufferedWriter(fw);
					writer.write(translate(currentFASTA, frameNum).toString());
					writer.close();
				}
				
				//Write the superGFF
				File gffToWrite = new File("results/" + SUPER_GFF + "" + supernumber + ".gff");
				gffToWrite.createNewFile();
				fw = new FileWriter(gffToWrite.getAbsoluteFile());
				writer = new BufferedWriter(fw);
				
				writer.write(currentGFF.writeGFF());
				writer.close();
				
				supernumber++;
				currentFASTA = new StringBuilder(toAppend);
				currentGFF.genes = toAppendGFF;
			}
			
		}
		
		deleteTempDir(fastaDir);
		deleteTempDir(gffDir);
		
		return "Complete";
	}
	
	
	public static boolean geneMatch(Gene toCompare, List<Gene> current) {
		
		for(Gene g : current) {
			if(toCompare.getName().equals(g.getName())) {
				if(toCompare.getIsoforms().size() == g.getIsoforms().size()) {
					if(matchIsoforms(toCompare.getIsoforms(), g.getIsoforms())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean matchIsoforms(List<Isoform> a, List<Isoform> b) {
		int i = 0;
		
		for(Isoform i1 : a) {
			for(Isoform i2 : b) {
				if(i1.getName().equals(i2.getName())) {
					i++;
					continue;
				}
			}
		}
		
		return i == a.size();
	}
	
	public static StringBuilder translate(StringBuilder dna, int frame) {
		
		char a, b;
		StringBuilder rtn;
		StringBuilder reverseDNA;
		
		switch (frame) {
		case 1:
			return translateHelper(dna);
		case 2:
			a = dna.charAt(0);
			dna.deleteCharAt(0);
			rtn = translateHelper(dna);
			dna.insert(0, a);
			return rtn;
		case 3:
			a = dna.charAt(0);
			dna.deleteCharAt(0);
			b = dna.charAt(0);
			dna.deleteCharAt(0);
			rtn = translateHelper(dna);
			dna.insert(0, b);
			dna.insert(0, a);
			return rtn;
		case 4:
			reverseDNA = doReverseCompliment(dna);
			return translateHelper(reverseDNA);
		case 5:
			reverseDNA = doReverseCompliment(dna);
			a = reverseDNA.charAt(0);
			reverseDNA.deleteCharAt(0);
			rtn = translateHelper(reverseDNA);
			reverseDNA.insert(0, a);
			return rtn;
		case 6:
			reverseDNA = doReverseCompliment(dna);
			a = reverseDNA.charAt(0);
			reverseDNA.deleteCharAt(0);
			b = reverseDNA.charAt(0);
			reverseDNA.deleteCharAt(0);
			rtn = translateHelper(reverseDNA);
			reverseDNA.insert(0, b);
			reverseDNA.insert(0, a);
			return rtn;
		}
		
		return translateHelper(dna);
	}
	
	
	/**
	 * Translates dna strands to protiens
	 * 
	 * @param dna
	 * @return
	 */
	public static StringBuilder translateHelper(StringBuilder dna) {
		StringBuilder rtn = new StringBuilder();
		
		if(protienMap.get("CTG") == null) {
			initProtienMap();
		}
		
		for(int i = 1; i < dna.length()+1; i++) {
			if(i % 3 == 0) {
				
				if(protienMap.get(dna.substring(i-3, i)) == null) {
//					System.out.println(dna.substring(i-3, i));
				} else {
					rtn.append(protienMap.get(dna.substring(i-3, i)));	
				}
			}
		}
		
		return rtn;
	}
	
	public static StringBuilder doReverseCompliment(StringBuilder sequence) {

		StringBuilder complimentBuilder = new StringBuilder();

		for (int i = sequence.length()-1; i >= 0; i--) {
			complimentBuilder.append(getCompliment(sequence.charAt(i)));
		}

		return complimentBuilder;
	}
	
	public static char getCompliment(char c) {
		switch (c) {
		case 'T':
			return 'A';
		case 'A':
			return 'T';
		case 'G':
			return 'C';
		case 'C':
			return 'G';
		}
		return 'N';
	}
	
	
	public static void initProtienMap() {
		protienMap.put("TTT", "F");
		protienMap.put("TTC", "F");
		protienMap.put("TTA", "L");
		protienMap.put("TTG", "L");
		protienMap.put("TCT", "S");
		protienMap.put("TCC", "S");
		protienMap.put("TCA", "S");
		protienMap.put("TCG", "S");
		protienMap.put("TAT", "Y");
		protienMap.put("TAC", "Y");
		protienMap.put("TAA", "#");
		protienMap.put("TAG", "#");
		protienMap.put("TGT", "C");
		protienMap.put("TGC", "C");
		protienMap.put("TGA", "#");
		protienMap.put("TGG", "W");
		protienMap.put("CTT", "L");
		protienMap.put("CTC", "L");
		protienMap.put("CTA", "L");
		protienMap.put("CTG", "L");
		protienMap.put("CCT", "P");
		protienMap.put("CCC", "P");
		protienMap.put("CCA", "P");
		protienMap.put("CCG", "P");
		protienMap.put("CAT", "H");
		protienMap.put("CAC", "H");
		protienMap.put("CAA", "Q");
		protienMap.put("CAG", "Q");
		protienMap.put("CGT", "R");
		protienMap.put("CGC", "R");
		protienMap.put("CGA", "R");
		protienMap.put("CGG", "R");
		protienMap.put("ATT", "I");
		protienMap.put("ATC", "I");
		protienMap.put("ATA", "I");
		protienMap.put("ATG", "M");
		protienMap.put("ACT", "T");
		protienMap.put("ACC", "T");
		protienMap.put("ACA", "T");
		protienMap.put("ACG", "T");
		protienMap.put("AAT", "N");
		protienMap.put("AAC", "N");
		protienMap.put("AAA", "K");
		protienMap.put("AAG", "K");
		protienMap.put("AGT", "S");
		protienMap.put("AGC", "S");
		protienMap.put("AGA", "R");
		protienMap.put("AGG", "R");
		protienMap.put("GTT", "V");
		protienMap.put("GTC", "V");
		protienMap.put("GTA", "V");
		protienMap.put("GTG", "V");
		protienMap.put("GCT", "A");
		protienMap.put("GCC", "A");
		protienMap.put("GCA", "A");
		protienMap.put("GCG", "A");
		protienMap.put("GAT", "D");
		protienMap.put("GAC", "D");
		protienMap.put("GAA", "E");
		protienMap.put("GAG", "E");
		protienMap.put("GGT", "G");
		protienMap.put("GGC", "G");
		protienMap.put("GGA", "G");
		protienMap.put("GGG", "G");
	}
	
	/**
	 * Does global alignment for gene sequences. Returns 1 if the first argument
	 * is closer to the t string, otherwise returns 2
	 * 
	 * @param s1
	 * @param s2
	 * @param t
	 * @return
	 */
	public static int globalAlign(String s1, String s2, String t) {
		
		int one = globalAlignHelper(s1, t);
		int two = globalAlignHelper(s2, t);
		
		return one > two ? 1 : 2;
	}
	
	/**
	 * 
	 * Returns the score of the global alignment between the two strings
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static int globalAlignHelper(String one, String two) {
		int d = -4;
		int matrix[][] = new int[one.length()+1][two.length()+1];
		
		for(int i = 0; i <= one.length(); i++) {
			matrix[i][0] = d*i;
		}
		for(int j = 0; j <= two.length(); j++) {
			matrix[0][j] = d*j;
		}
		
		for(int i = 1; i <= one.length(); i++) {
			for(int j = 1; j <= two.length(); j++) {
				
				boolean match = one.charAt(i-1) == two.charAt(j-1);
				int diag = matrix[i-1][j-1] + (match ? 5 : d);
				int h = matrix[i-1][j] + d;
				int v = matrix[i][j-1] + d;
				
				if(h >= diag && h >= v) {
					matrix[i][j] = h;
				}
				if(v >= diag && v >= h) {
					matrix[i][j] = v;
				}
				if(diag >= h && diag >= v) {
					matrix[i][j] = diag;
				}
			}
		}
		
		return matrix[one.length()][two.length()];
	}
	
	/**
	 * Sorts files in an array based on trailing number so
	 * we read the files in order
	 */
	public static void sortFiles(File list[]) {
		Arrays.sort(list, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				
				int fileNum1 = findFileNum(o1.getName());
				int fileNum2 = findFileNum(o2.getName());
				
				return fileNum1 - fileNum2;
			}
		});
	}
	
	/**
	 * returns the number of the file
	 * used for sorting files 
	 */
	public static int findFileNum(String name) {
		int dotIndex = name.indexOf(".");
		int numIndex = dotIndex-1;
		
		boolean foundNum = true;
		
		while(foundNum) {
			try {
				numIndex--;
				Integer.parseInt(name.charAt(numIndex) + "");
			} catch(Exception e) {
				foundNum = false;
			}
		}
		
		try {
			return Integer.parseInt(name.substring(numIndex+1, dotIndex));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * Takes two FASTA strings and merges them into a single
	 * FASTA string the first parameter is modified
	 * 
	 * @param one
	 * @param two
	 * @return overlap value if there is overlap else -1
	 */
	public static int mergeStrings(StringBuilder one, StringBuilder two) {
		
		for(int i = 0; i < one.length(); i++) {
			if(compareSubstrings(i, one, two)) {
				int overlap = one.length() - i;				
				
				one.append(two.substring(overlap));
				
				return overlap;
			}
		}
		
		return -1;
	}
	
	public static boolean compareSubstrings(int s1Index, StringBuilder s1, StringBuilder s2) {
		int i = s1Index;
		int j = 0;
		while(i < s1.length()) {
			if(s1.charAt(i) != s2.charAt(j)) {
				return false;
			}
			
			i++;
			j++;
		}
		
		return true;
	}
	
	/**
	 * Creates temporary directory where we will read files from
	 * 
	 * @param tempName
	 * @param zipPath
	 */
	private static File createTempDir(String tempName, String zipPath) {
		try {
			ZipFile zip = new ZipFile(zipPath);
			zip.extractAll(tempName);
			File dir = new File(tempName);
			File newDir = dir.listFiles()[0];
			return newDir;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Deletes a directory
	 * 
	 * @param dirPath
	 */
	private static void deleteTempDir(File dir) {
		try {
			delete(dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void delete(File file) throws Exception {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				System.out.println("Directory is deleted : "
						+ file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					System.out.println("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}
}
