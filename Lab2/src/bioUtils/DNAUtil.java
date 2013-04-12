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

    public static double findGcContentForSegment(String segment) {	

	int total;
	int gc;
	int n;
	
	for(int i = 0; i < segment.length(); i++) {
	    total++;
	    if (segment.charAt(i) == 'G' || segment.charAt(i) == 'C') {
		gc++;
	    } else if (segment.charAt(i) == 'N') {
		n++;
	    }
	}

	return (double) gc / total;
    }
}
