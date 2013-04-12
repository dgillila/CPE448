package bioUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DNAUtil {

	public static String analyze(String dnaFile, int startIndex, int stopIndex, int winShift, int winSize) throws Exception {

		final ExecutorService service;
		final Future<Result> task;
		
		Map<Integer, Result> results = new ConcurrentHashMap<Integer, Result>();
		File file = new File(dnaFile);
		
		service = Executors.newFixedThreadPool(100);
		
		task = service.submit(new CalcThread(file, startIndex, winSize));
		
		try {
			int curIndex = 0;
			while(true) {
				Result temp = service.submit(new CalcThread(file, curIndex, winSize)).get();
				results.put(curIndex, temp);
			}
		} catch (Exception e) {
			
		}
		
		
		Scanner fileScan;
		try {
			fileScan = new Scanner(file);

			// Skip first line - does not contain sequence
			fileScan.nextLine();

			while (fileScan.hasNextLine()) {
				// sequence += fileScan.nextLine();
			}

			fileScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Test";
	}

	public static double findGcContentForSegment(String segment) {

		int total = 0;
		int gc = 0;
		int n = 0;

		for (int i = 0; i < segment.length(); i++) {
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
