package bioUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Scanner;

import bioUtils.Result;

public class CalcThread implements Runnable {

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
		BufferedInputStream stream = null;
		
		try {
			scan = new Scanner(f);
			
			FileInputStream fStream = new FileInputStream(f);
			stream = new BufferedInputStream(fStream);
			// Skip first line - does not contain sequence
			String header = scan.nextLine();
			byte[] temp = new byte[header.length()];
			stream.read(temp);
			
			//skip any other special characters
			int waste = 0;
			boolean lazyFlag = false;
			while(waste < 40) {
				lazyFlag=true;
				stream.mark(2);
				waste = stream.read();
			}
			if(lazyFlag) {
				stream.reset();
			}
			//We're now at index 0 (one before characters start)			
			// Skip to correct location in the file (exclude spaces and newlines)
			
			//TODO THIS IS SLOW (NEEDS TO BE REDONE)
			int i = 1;
			while(i < sIndex) {
				int b = stream.read();
				if(b > 48) {
					i++;
				}
				if(b == -1) {
					stream.close();
					return;
				}
			}
			
			//Read in a string of windowSize (exclude spaces and newlines)
			//TODO THIS IS ALSO SLOW (SAME THING)
			i = 0;
			temp = new byte[winSize];
			while(i < winSize) {
				int b = stream.read();
				if(b > 48) {
					temp[i] = (byte)b;
					i++;
				}
				if(b == -1) {
					break;
				}
			}
			
			String segment = new String(temp);
			
			Result res = DNAUtil.findGcContentForSegment(segment);
			
			resMap.put(sIndex, res);
			
			stream.close();
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

}
