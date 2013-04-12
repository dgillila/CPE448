import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import bioUtils.CalcThread;
import bioUtils.Result;

public class ThreadTest {

	public static void main(String[] args) {

		final ExecutorService service;
		final Future<Result> task;

		File f = new File("C:\\Users\\Daniel\\Documents\\test2.txt");

		service = Executors.newFixedThreadPool(5);
//		task = service.submit(new CalcThread(f));
		
		try {
//			System.out.println(task.get().min);
		} catch (Exception e) {
			
		}

//		new CalcThread(f).run();
//		new CalcThread(f).run();

	}

}
