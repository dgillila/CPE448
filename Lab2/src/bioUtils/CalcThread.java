package bioUtils;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;

import bioUtils.Result;

public class CalcThread implements Callable<Result> {

	protected File f = null;
	protected int sIndex = 0;
	protected int winSize = 0;

	public CalcThread(File val, int startIndex, int windowSize) {
		this.f = val;
		this.sIndex = startIndex;
		this.winSize = windowSize;
	}

	public void run() {
		try {
			Scanner scan = new Scanner(f);

			System.out.println(scan.next());
		} catch (Exception e) {

		}
	}

	@Override
	public Result call() throws Exception {
		Result rtn = new Result();
		
		Scanner scan = new Scanner(f);

		System.out.println(scan.next());
		
		rtn.min = 0.3;
		rtn.max = 3.4;
		
		return rtn;
	}

}
