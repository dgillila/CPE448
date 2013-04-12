package bioUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DNAUtil {
	
	public static String analyze(String dnaFile, int startIndex, int stopIndex, int winShift, int winSize) throws Exception {
				
		File file = new File(dnaFile);
		Scanner fileScan;
		try {
			fileScan = new Scanner(file);

			// Skip first line - does not contain sequence
			fileScan.nextLine();

			while (fileScan.hasNextLine()) {
//				sequence += fileScan.nextLine();
			}

			fileScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return "Test";
	}

}
