package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import net.lingala.zip4j.core.ZipFile;

public class DNAConcat {
	
	private static String FASTA_DIR = "~tempFASTA";
	private static String GFF_DIR = "~tempGFF";
	
	private static String SUPER_CONTIG = "supercontig";
	private static String SUPER_GFF = "supergff";
	
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
		
		File supercontig;
		File superGFF;
		
		int supernumber = 1;
		
		String currentFASTA = "";
		
		File fastaFiles[] = fastaDir.listFiles();
		
		sortFiles(fastaFiles);
		
		if(fastaFiles.length > 0) {
			currentFASTA = DNAFileReader.readFASTA(fastaFiles[0].getAbsolutePath());
		}
		
		for(int i = 1; i < fastaFiles.length - 1; i++) {
			File f = fastaFiles[i];
			String toAppend = DNAFileReader.readFASTA(f.getAbsolutePath());
			
			int overlap = mergeStrings(new StringBuilder(currentFASTA), new StringBuilder(toAppend));
			
			//TODO play with the GFF's and fix them up
			
			if(overlap < 0) {
				//Write the supercontig
				File fastaToWrite = new File("results/" + SUPER_CONTIG + "" + supernumber + ".fasta");
				fastaToWrite.createNewFile();
				FileWriter fw = new FileWriter(fastaToWrite.getAbsoluteFile());
				BufferedWriter writer = new BufferedWriter(fw);

				writer.write(currentFASTA);
				
				//Write the superGFF
				
				writer.close();
				supernumber++;
				currentFASTA = toAppend;
			}
			
		}
		
		deleteTempDir(fastaDir);
		deleteTempDir(gffDir);
		
		return null;
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
	
	//Start at one again for the new GFF and Supercontig when a gap is found
	
}
