package bioUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jxl.Cell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import net.lingala.zip4j.core.ZipFile;

public class DNAUtil {

//	public static String singleAnalyze(String dnaFile, int startIndex,
//			int stopIndex, int winShift, int winSize) {
//
//		Map<Integer, Result> results = new TreeMap<Integer, Result>();
//		File file = new File(dnaFile);
//
//		// Check and initialize all variables
//		int curIndex = startIndex < 0 ? 1 : startIndex;
//		int lastIndex = stopIndex < 0 ? -1 : stopIndex;
//		int size = winSize < 0 ? 1000 : winSize;
//		int shift = winShift < 0 ? size : winShift;
//
//		BufferedReader reader = null;
//		try {
//			FileReader input = new FileReader(file);
//			reader = new BufferedReader(input);
//
//			// Skip the first line
//			reader.readLine();
//			// Get to the start index
//			for (int i = 1; i < curIndex;) {
//				int res = reader.read();
//				if (res > 40) {
//					i++;
//				}
//				if (res == -1) {
//					input.close();
//					return "";
//				}
//			}
//
//			boolean end = false;
//			boolean first = true;
//			int runningIndex = curIndex;
//
//			// While we still have characters to read
//			while (!end) {
//
//				// reset the reader
//				if (!first) {
//					try {
//						reader.reset();
//					} catch (Exception e) {
//						// Don't care - ignore this
//					}
//
//					// seek - not on the first iteration
//					for (int i = 0; i < shift;) {
//						int res = reader.read();
//						if (res > 40) {
//							i++;
//						}
//						if (res == -1) {
//							end = true;
//							break;
//						}
//					}
//				}
//
//				first = false;
//
//				// mark
//				StringBuilder sequence = new StringBuilder();
//				reader.mark(size + 1);
//
//				int i;
//				// read the next winsize sequence
//				for (i = 0; i < size && runningIndex + i < lastIndex;) {
//					int res = reader.read();
//					if (res > 40) {
//						sequence.append((char) res);
//						i++;
//					}
//					if (res == -1) {
//						end = true;
//						break;
//					}
//				}
//
//				if (runningIndex + shift <= lastIndex) {
//					Result result = DNAUtil.findGcContentForSegment(sequence
//							.toString());
//
//					results.put(runningIndex, result);
//				}
//
//				runningIndex += shift;
//
//				if (runningIndex >= lastIndex) {
//					end = true;
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		DecimalFormat df = new DecimalFormat("#.##");
//
//		// And build the result string
//		String rtn = "";
//
//		if (winShift > 0 || winSize > 0) {
//			rtn = "Range" + " ,\t" + "min" + " ,\t" + "max\n";
//
//			// Sort the results for better display
//			Map<Integer, Result> sortedRes = new TreeMap<Integer, Result>(
//					results);
//
//			for (Integer a : sortedRes.keySet()) {
//				rtn += a + " to " + (a + size - 1) + " ,\t"
//						+ df.format(results.get(a).min) + " ,\t"
//						+ df.format(results.get(a).max) + "\n";
//			}
//		} else {
//			double minTot = 0;
//			double maxTot = 0;
//
//			for (Integer a : results.keySet()) {
//				minTot += results.get(a).min;
//				maxTot += results.get(a).max;
//			}
//			rtn = "Total min ,\tTotal max\n";
//			rtn += df.format(minTot / results.keySet().size()) + " ,\t"
//					+ df.format(maxTot / results.keySet().size());
//		}
//
//		return rtn;
//	}
		
	public static void analyzeZip(String dnaZip, int startIndex, int stopIndex,
			int winShift, int winSize) throws Exception {
		
		ZipFile zip = new ZipFile(dnaZip);
		zip.extractAll("~temp");
		
		File dir = new File("~temp");
		File newDir = dir.listFiles()[0];
		
		File output = new File("results.xls");
		
		WorkbookSettings wbSettings = new WorkbookSettings();
	    wbSettings.setLocale(new Locale("en", "EN"));
	    WritableWorkbook workbook = Workbook.createWorkbook(output, wbSettings);
		int i = 0;		
		double totalMin = 0;
		double totalMax = 0;
		
		for(File filePath : newDir.listFiles()) {
			String result = analyze(filePath.getAbsolutePath(), startIndex, stopIndex, winShift, winSize);
			workbook.createSheet(filePath.getName().substring(0, filePath.getName().indexOf(".")), i);
			WritableSheet sheet = workbook.getSheet(i);
			i++;
			
			String lines[] = result.split("\n");
			int row = 0;
			for(String line : lines) {
				String vals[] = line.split(" ,\t");
				//For no sliding window
				int j = 0;
				for(String val : vals) {
					Label l = new Label(j, row, val);
					sheet.addCell(l);
					j++;
				}
				
				if(result.charAt(0) == 'T' && row == 1) {
					totalMin += Double.parseDouble(vals[0]);
					totalMax += Double.parseDouble(vals[1]);
				}
				
				row++;
			}
		}
		
		totalMin = totalMin / newDir.list().length;
		totalMax = totalMax / newDir.list().length;
		
		workbook.createSheet("Summary", 0);
		WritableSheet s = workbook.getSheet(0);
		
		s.addCell(new Label(0, 0, "TOTAL MIN"));
		s.addCell(new Label(1, 0, "TOTAL MAX"));
		s.addCell(new Label(0, 1, ""+totalMin));
		s.addCell(new Label(1, 1, ""+totalMax));
		
		workbook.write();
		workbook.close();
		
		try {
			delete(dir);
		} catch(Exception e) {
			System.err.println("Directory not deleted");
		}
		
	}

	public static String analyze(String dnaFile, int startIndex, int stopIndex,
			int winShift, int winSize) throws Exception {

		//final ExecutorService service;

		Map<Integer, Result> results = new ConcurrentHashMap<Integer, Result>();
		//File file = new File(dnaFile);

		// Create the thread pool
		//service = Executors.newFixedThreadPool(100);
		
		String dnaSequence = DNAFileReader.readFASTA(dnaFile);

		// Check and initialize all variables
		int curIndex = startIndex < 0 ? 1 : startIndex;
		int lastIndex = stopIndex < 0 ? dnaSequence.length() : stopIndex;
		int size = winSize < 0 ? 1000 : winSize;
		int shift = winShift < 0 ? size : winShift;
		int endPos = 0;
		
		while(curIndex <= lastIndex)
		{
			
			endPos = (curIndex + size <= lastIndex) ? (curIndex+size-1) : lastIndex;
			
			Result res = DNAUtil.findGcContentForSegment(dnaSequence.substring(curIndex-1, endPos));
			res.start = curIndex;
			res.stop = endPos;
			if(!res.max.isNaN() && !res.min.isNaN()) {
				results.put(curIndex, res);
			}
			curIndex += shift;
		}

		
		// Kick off the threads
		/*
		for (int i = 0; i < size / shift; i++) {
			service.execute(new CalcThread(file, curIndex, lastIndex, size,
					results));
			curIndex += shift;
		}
		*/

		// Wait until all threads terminate (max wait of 1 minute - may need to
		// make this longer)
		//service.shutdown();
		//service.awaitTermination(1, TimeUnit.MINUTES);

		DecimalFormat df = new DecimalFormat("#.##");

		// And build the result string
		String rtn = "";

		if (winShift > 0 || winSize > 0) {
			rtn = "Range" + " ,\t" + "min" + " ,\t" + "max\n";

			// Sort the results for better display
			Map<Integer, Result> sortedRes = new TreeMap<Integer, Result>(
					results);

			for (Integer a : sortedRes.keySet()) {
				rtn += results.get(a).start + " to " + results.get(a).stop + " ,\t" //a + " to " + (a + size - 1) + " ,\t"
						+ df.format(results.get(a).min) + " ,\t"
						+ df.format(results.get(a).max) + "\n";
			}
		} else {
			double minTot = 0;
			double maxTot = 0;

			for (Integer a : results.keySet()) {
				minTot += results.get(a).min;
				maxTot += results.get(a).max;
			}
			rtn = "Total min ,\tTotal max\n";
			rtn += df.format(minTot / results.keySet().size()) + " ,\t"
					+ df.format(maxTot / results.keySet().size());
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
