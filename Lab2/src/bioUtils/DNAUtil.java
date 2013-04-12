package bioUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
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
		int shift = winShift < 0 ? size : winShift;

		//Kick off the threads (maybe stop when curIndex + winSize > lastIndex ?)
		for(int i = 0; i < 6; i++) {
			service.execute(new CalcThread(file, curIndex, size, results));
			curIndex += shift;
		}

		//Wait until all threads terminate (max wait of 1 minute - may need to make this longer)
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);

		//Sort the results for better display
		Map<Integer, Result> sortedRes = new TreeMap<Integer, Result>(results);
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		//And build the result string
		String rtn = "Range" + " ,\t" + "min" + " ,\t" + "max\n";
		for(Integer a : sortedRes.keySet()) {
			rtn += a + " - " + (a+size) + " ,\t" + df.format(results.get(a).min) + " ,\t" + df.format(results.get(a).max) + "\n";
		}

		return rtn;
	}

	public static Result findGcContentForSegment(String segment) {

		double total = 0;
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
		rtn.max = 100 * (double) (gc + n) / total;
		rtn.min = 100 * (double) gc / total;

		return rtn;
	}
}
