package utils;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;

public class DNAConcat {
	
	private static String FASTA_DIR = "~tempFASTA";
	private static String GFF_DIR = "~tempGFF";
	
	/**
	 * Does the main processing of the files
	 * 
	 * @param fastaZip
	 * @param gffZip
	 * @return
	 */
	public static String processFiles(String fastaZip, String gffZip) {
		
		File fastaDir = createTempDir(FASTA_DIR, fastaZip);
		File gffDir = createTempDir(GFF_DIR, gffZip);
		
		
		
		
		
		deleteTempDir(fastaDir);
		deleteTempDir(gffDir);
		
		return null;
	}
	
	/**
	 * Takes two FASTA strings and merges them into a single
	 * FASTA string
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	private static String mergeStrings(String one, String two) {
		
		
		return null;
	}
	
	public static boolean compareSubstrings(int s1Index, String s1, String s2) {
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
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}
	
	//Start at one again for the new GFF and Supercontig when a gap is found
	
}
