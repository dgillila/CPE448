package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import net.lingala.zip4j.core.ZipFile;

import model.Gene;

public class DNAUtil {

	public static double avgCDSSpan = 0;
	public static double avgCDSSize = 0;
	public static double avgExonSize = 0;
	public static double avgIntronSize = 0;
	public static double avgIntergenicSize = 0;
	public static int totalNucleotides = 0;
	public static int numGenes = 0;
	public static int totalCDSActual = 0;
	public static double totalT = 0;

	// Static methods again works for me

	public static String calculateZip(String zipPath) throws Exception {

		ZipFile zip = new ZipFile(zipPath);
		zip.extractAll("~temp");
		File dir = new File("~temp");
		File newDir = dir.listFiles()[0];

		File output = new File("results.xls");

		double val1 = 0;
		double val2 = 0;
		double val3 = 0;
		double val4 = 0;
		double val5 = 0;
		double val6 = 0;
		double val7 = 0;
		double val8 = 0;
		double val9 = 0;
		double val10 = 0;

		int i = 0;

		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(output, wbSettings);

		for (File f : newDir.listFiles()) {
			String result = calculateResults(f.getAbsolutePath());
			workbook.createSheet(
					f.getName().substring(0, f.getName().indexOf(".")), i);
			WritableSheet sheet = workbook.getSheet(i);
			i++;

			String lines[] = result.split("\n");
			int row = 0;
			for (String line : lines) {
				String vals[] = line.split("\t");
				int col = 0;
				for (String s : vals) {
					String moreVals[] = s.split("\\)");
					for (String last : moreVals) {
						if (col == 1) {
							try {
								switch (row) {
								case 1:
									val1 += Double.valueOf(last);
									break;
								case 2:
									val2 += Double.valueOf(last);
									break;
								case 3:
									val3 += Double.valueOf(last);
									break;
								case 4:
									val4 += Double.valueOf(last);
									break;
								case 5:
									val5 += Double.valueOf(last);
									break;
								case 8:
									val6 += Double.valueOf(last);
									break;
								case 9:
									val7 += Double.valueOf(last);
									break;
								case 10:
									val8 += Double.valueOf(last);
									break;
								case 11:
									val9 += Double.valueOf(last);
									break;
								case 12:
									val10 += Double.valueOf(last);
									break;
								default:
									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						sheet.addCell(new Label(col++, row, last));
					}
				}

				row++;
			}
		}

		workbook.createSheet("Summary", 0);
		WritableSheet s = workbook.getSheet(0);

		s.addCell(new Label(0, 0, "TOTAL GENE CONTENT"));
		s.addCell(new Label(1, 1, ""));
		s.addCell(new Label(1, 2, "TOTAL AVERAGE CDS SPAN"));
		s.addCell(new Label(0, 2, "" + val1 / i));

		s.addCell(new Label(1, 3, "TOTAL AVERAGE CDS SIZE"));
		s.addCell(new Label(0, 3, "" + val2 / i));

		s.addCell(new Label(1, 4, "TOTAL AVERAGE EXON SIZE"));
		s.addCell(new Label(0, 4, "" + val3 / i));

		s.addCell(new Label(1, 5, "TOTAL AVERAGE INTRON SIZE"));
		s.addCell(new Label(0, 5, "" + val4 / i));

		s.addCell(new Label(1, 6, "TOTAL AVERAGE INTERGENIC REGION SIZE"));
		s.addCell(new Label(0, 6, "" + val5 / i));

		s.addCell(new Label(1, 7, " "));
		s.addCell(new Label(0, 8, "TOTAL GENE DENSITY"));
		s.addCell(new Label(1, 9, "TOTAL NUMBER OF GENES"));
		s.addCell(new Label(0, 9, "" + val6 / i));

		s.addCell(new Label(1, 10, "TOTAL NUMBER OF GENES 2"));
		s.addCell(new Label(0, 10, "" + val7 / i));

		s.addCell(new Label(1, 11, "TOTAL CDS SIZE"));
		s.addCell(new Label(0, 11, "" + val8 / i));

		s.addCell(new Label(1, 12, "TOTAL NUMBER OF GENES PER KBPAIRS"));
		s.addCell(new Label(0, 12, "" + val9 / i));

		s.addCell(new Label(1, 13, "TOTAL KBPAIRS/NUMBER OF GENES"));
		s.addCell(new Label(0, 13, "" + val10 / i));

		workbook.write();
		workbook.close();

		try{
			delete(dir);
		} catch(Exception e) {
			System.err.println("DIRECTORY NOT DELETED");
		}

		return "RESULTS FILE CREATED (In the same directory as this program)";
	}

	public static String calculateResults(String filepath) throws Exception {

		avgCDSSize = 0;
		avgCDSSpan = 0;
		avgExonSize = 0;
		avgIntronSize = 0;
		avgIntergenicSize = 0;
		totalNucleotides = 0;
		numGenes = 0;
		totalCDSActual = 0;
		totalT = 0;

		// Name of the gene to all of the attributes of it
		Map<String, List<Gene>> genes = new HashMap<String, List<Gene>>();

		readFile(filepath, genes);

		calculateGeneContentAndDensity(genes);

		return displayInfo(genes);
	}

	private static String displayInfo(Map<String, List<Gene>> genes) {
		String rtn = "";

		DecimalFormat df = new DecimalFormat("#.##");
		// for(String name : genes.keySet()) {
		// rtn += (name + " " + genes.get(name).size() + "\n");
		// }
		rtn += "Gene Content:\n";
		rtn += "a) " + df.format(avgCDSSpan) + "\tAverage CDS Span \n";
		rtn += "b) " + df.format(avgCDSSize) + "\tAverage CDS Size \n";
		rtn += "c) " + df.format(avgExonSize) + "\tAverage Exon Size \n";
		rtn += "d) " + df.format(avgIntronSize) + "\tAverage Intron Size \n";
		rtn += "e) " + df.format(avgIntergenicSize)
				+ "\tAverage Intergenic Region Size \n";

		rtn += "\n2. Gene Density:\n";

		rtn += "a) "
				+ df.format(numGenes * (avgCDSSpan / totalT))
				+ "\tNumber of Genes * (Average CDS Span / total nucleotides)\n";
		rtn += "b) "
				+ df.format(numGenes * (avgCDSSize / totalT))
				+ "\tNumber of Genes * (Average CDS Size / total nucleotides)\n";
		rtn += "c) " + df.format((double) totalCDSActual / totalT)
				+ "\tTotal CDS Size (combining isoforms) / total nucleotides\n";
		rtn += "d) " + df.format((double) numGenes / (totalT / 1000.0))
				+ "\tNumber of Genes per KBPairs\n";
		rtn += "e) " + df.format((totalT / 1000.0) / numGenes)
				+ "\tKBPairs / Number of Genes \n";

		return rtn;
	}

	private static void readFile(String filepath, Map<String, List<Gene>> genes)
			throws Exception {
		// Set up the file reader
		File f = new File(filepath);
		FileReader in = new FileReader(f);
		BufferedReader reader = new BufferedReader(in);

		String line = null;
		do {
			line = reader.readLine();

			if (line != null) {
				// Break the line apart by the tabs
				String[] tokens = line.split("\t");

				// Create a new Gene to save the parts
				Gene g = new Gene();

				// Set all the normal stuff
				g.setChromosomeName(tokens[0]);
				g.setSource(tokens[1]);

				if (!tokens[2].equals("mRNA") && !tokens[2].equals("CDS")) {
					continue;
				}

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

					if (key.equals("gene_id")) {
						g.setName(val.replace("\"", ""));
						g.addAttribute(key, val.replace("\"", ""));
					} else {
						g.addAttribute(key, val.replace("\"", ""));
					}
				}

				// Update start or stop based on + and - values
				if (g.getFeature().equals("mRNA")) {
					if (g.isForwardStrand()) {
						g.setStop(g.getStop() + 3);
					} else {
						g.setStart(g.getStart() - 3);
					}
				}

				// The gene is finished reading save it to the gene map
				if (genes.get(g.getName()) == null) {
					List<Gene> temp = new ArrayList<Gene>();
					temp.add(g);
					genes.put(g.getName(), temp);
				} else {
					genes.get(g.getName()).add(g);
				}

			}
		} while (line != null);

		// All done
		reader.close();
	}

	private static void calculateGeneContentAndDensity(
			Map<String, List<Gene>> genes) {
		List<Gene> gList;
		int mRNATotalSize = 0;
		int mRNACount = 0;
		Map<String, Double> averageCDSSizes = new HashMap<String, Double>();
		Map<String, Double> averageExonSizes = new HashMap<String, Double>();
		Map<String, AttributeInfo> isoforms = new HashMap<String, AttributeInfo>();
		Map<String, AttributeInfo> geneInfos = new HashMap<String, AttributeInfo>();
		int totalCDS = 0;
		int totalIntergenicRegionSize = 0;
		List<Gene> mRNAs = new ArrayList<Gene>();
		boolean foundRNA = false;
		// Lazy flag ... haha
		boolean first = true;
		int maxEndPos = 0;
		int smallestStartPos = 0;
		List<IntegerPair> cdsRegions = new ArrayList<IntegerPair>();

		for (String name : genes.keySet()) {
			gList = genes.get(name);
			foundRNA = false;
			for (Gene g : gList) {
				// Average CDS Span
				if (g.getFeature().equals("mRNA")) {
					if (!foundRNA) {
						mRNAs.add(g);
						foundRNA = true;
					}
					mRNACount++;
					mRNATotalSize += g.getStop() - g.getStart();
				} else if (g.getFeature().equals("CDS")) {
					totalCDS++;
					cdsRegions.add(new IntegerPair(g.getStart(), g.getStop()));

					// Average CDS size for all isoforms
					String transcriptID = g.getAttributes()
							.get("transcript_id");
					if (isoforms.containsKey(transcriptID)) {
						isoforms.get(transcriptID).add(
								g.getStop() - g.getStart());
					} else {
						isoforms.put(transcriptID, new AttributeInfo(
								transcriptID, g.getStop() - g.getStart()));
					}

					// Average Exon Size for each gene that is a CDS
					String geneID = g.getAttributes().get("gene_id");
					if (geneInfos.containsKey(geneID)) {
						geneInfos.get(geneID).add(g.getStop() - g.getStart());
					} else {
						geneInfos.put(
								geneID,
								new AttributeInfo(geneID, g.getStop()
										- g.getStart()));
					}
				}

				if (first) {
					maxEndPos = g.getStop();
					smallestStartPos = g.getStart();
				} else {
					maxEndPos = (g.getStop() > maxEndPos) ? g.getStop()
							: maxEndPos;
					smallestStartPos = (g.getStart() < smallestStartPos) ? g
							.getStart() : smallestStartPos;
				}
			}
		}

		// actual total cds size
		Collections.sort(cdsRegions, new Comparator<IntegerPair>() {
			@Override
			public int compare(IntegerPair o1, IntegerPair o2) {
				if (o1.start < o2.start) {
					return -1;
				} else if (o1.start > o2.start) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		// find and combine overlapping regions
		IntegerPair curVal = cdsRegions.size() > 0 ? cdsRegions.get(0) : null;
		int cdsMin = curVal != null ? curVal.start : 0;
		int cdsMax = curVal != null ? curVal.stop : 0;

		Iterator<IntegerPair> iter = cdsRegions.iterator();
		while (iter.hasNext()) {
			IntegerPair temp = iter.next();
			while (temp.start <= curVal.stop && iter.hasNext()) {
				if (temp.stop > curVal.stop) {
					curVal.stop = temp.stop;
				}
				temp = iter.next();
			}

			totalCDSActual += (curVal.stop - curVal.start) + 1;
			if (curVal.start < cdsMin) {
				cdsMin = curVal.start;
			}
			if (curVal.stop > cdsMax) {
				cdsMax = curVal.stop;
			}
			curVal = temp;
		}

		totalT = cdsMax - cdsMin + 1;

		// Average intergenic region size
		Collections.sort(mRNAs, new Comparator<Gene>() {
			@Override
			public int compare(Gene o1, Gene o2) {
				if (o1.getStart() < o2.getStart()) {
					return -1;
				} else if (o1.getStart() > o2.getStart()) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		int lastStop = -1;

		for (Gene rna : mRNAs) {
			if (lastStop < 0) {
				lastStop = rna.getStop();
			} else {
				totalIntergenicRegionSize += rna.getStart() - lastStop;
				lastStop = rna.getStop();
			}
		}

		if (mRNAs.size() == 1) {
			avgIntergenicSize = 0;
		} else {
			avgIntergenicSize = totalIntergenicRegionSize / (mRNAs.size() - 1);
		}

		// Average CDS Span
		avgCDSSpan = (double) mRNATotalSize / mRNACount;

		// Find total CDS size per isoform
		for (String name : isoforms.keySet()) {
			AttributeInfo iso = isoforms.get(name);
			averageCDSSizes.put(iso.id, (double) iso.totalSize);
		}

		// Find average of all isoform CDS size averages
		for (String name : averageCDSSizes.keySet()) {
			avgCDSSize += averageCDSSizes.get(name);
		}

		// Average CDS size for all isoforms
		avgCDSSize /= isoforms.keySet().size();

		// Total Exon Size for each gene
		for (String name : geneInfos.keySet()) {
			AttributeInfo geneInfo = geneInfos.get(name);
			averageExonSizes.put(geneInfo.id, (double) geneInfo.totalSize);
		}

		// Find average of all Gene Exon size averages
		for (String name : averageExonSizes.keySet()) {
			avgExonSize += averageExonSizes.get(name);
		}

		// Average Exon Size for all genes
		avgExonSize /= totalCDS;

		// Average Intron Size
		avgIntronSize = (avgCDSSpan - avgCDSSize) / (totalCDS - 1);

		// Number of genes
		numGenes = geneInfos.keySet().size();

		// Finding highest Nucleotide number
		totalNucleotides = maxEndPos - smallestStartPos + 1;
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
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}

}
