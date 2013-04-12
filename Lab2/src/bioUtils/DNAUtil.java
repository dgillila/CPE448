package bioUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DNAUtil {

	public static String analyze(String dnaFile, int startIndex, int stopIndex,
			int winShift, int winSize) throws Exception {

		final ExecutorService service;

		Map<Integer, Result> results = new ConcurrentHashMap<Integer, Result>();
		File file = new File(dnaFile);

		// Create the thread pool
		service = Executors.newFixedThreadPool(100);

		// Check and initialize all variables
		int curIndex = startIndex < 0 ? 1 : startIndex;
		int lastIndex = stopIndex < 0 ? -1 : stopIndex;
		int size = winSize < 0 ? 1000 : winSize;
		int shift = winShift < 0 ? winSize : winShift;

		//Kick off the threads stop when curIndex + winSize > lastIndex
		for(int i = 0; i < 6; i++) {
			service.execute(new CalcThread(file, curIndex, size, results));
			curIndex += shift;
		}

		//Wait until all threads terminate
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);

		
		Map<Integer, Result> sortedRes = new TreeMap<Integer, Result>(results);
		for(Integer a : sortedRes.keySet()) {
			System.out.println(a);
		}
		
		// Scanner fileScan;
		// try {
		// fileScan = new Scanner(file);
		//
		// // Skip first line - does not contain sequence
		// fileScan.nextLine();
		//
		// while (fileScan.hasNextLine()) {
		// // sequence += fileScan.nextLine();
		// }
		//
		// fileScan.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return "Test";
	}

	public static Result findGcContentForSegment(String segment) {

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

		Result rtn = new Result();
		rtn.max = (double) gc + n / total;
		rtn.min = (double) gc / total;

		return rtn;
	}
}
