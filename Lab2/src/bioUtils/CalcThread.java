package bioUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

import bioUtils.Result;

public class CalcThread implements Callable<Result>, Runnable {

	protected File f = null;
	protected int sIndex = 0;
	protected int winSize = 0;
	protected Map<Integer, Result> resMap = null;

	public CalcThread(File val, int startIndex, int windowSize,
			Map<Integer, Result> map) {
		
		this.f = val;
		this.sIndex = startIndex;
		this.winSize = windowSize;
		this.resMap = map;
	}

	public void run() {
		
		Scanner scan = null;
		FileInputStream stream = null;
		
		try {
			scan = new Scanner(f);
			stream = new FileInputStream(f);

			// Skip first line - does not contain sequence
			String header = scan.nextLine();
			byte[] temp = new byte[header.length()];
			stream.read(temp);
			stream.read();
			
			//We're now at index 0 (one before characters start)			
			// Skip to correct location in the file
			temp = new byte[sIndex];
			stream.read(temp);
			
			//Read the window
//			System.out.println((char)stream.read());
			temp = new byte[winSize];
			stream.read(temp);
			
			String segment = new String(temp);
			
			Result res = DNAUtil.findGcContentForSegment(segment);
			
			resMap.put(sIndex, res);
			
			stream.close();
			scan.close();
		} catch (Exception e) {
			
		}
		return;
	}

	@Override
	public Result call() throws Exception {
		// Result rtn = new Result();
		//
		// Scanner scan = new Scanner(f);
		//
		// System.out.println(scan.next());
		//
		// rtn.min = 0.3;
		// rtn.max = 3.4;
		//
		return null;
	}

}
