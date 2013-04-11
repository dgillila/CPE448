import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

//	public static void main(String[] args) {
//		String file = "/Users/jimuir/Desktop/contig7.txt";
//		System.out.println("Start Reading File: " + file);
//		System.out.println(readFile(file));
//	}

	public static String readFile(String filename) {
		String sequence = new String();
		File file = new File(filename);
		Scanner fileScan;
		try {
			fileScan = new Scanner(file);

			// Skip first line - does not contain sequence
			fileScan.nextLine();

			while (fileScan.hasNextLine()) {
				sequence += fileScan.nextLine();
			}

			fileScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sequence;
	}

}