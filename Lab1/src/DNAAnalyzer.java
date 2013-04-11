import java.lang.String;

public class DNAAnalyzer {

	public static float GCContent(String segment, int start, int stop) throws Exception {
		int gc_count = 0;
		int total = 0;

		for (int i = start - 1; i < stop; i++) {
			total++;
			if (segment.charAt(i) == 'G' || segment.charAt(i) == 'C') {
				gc_count++;
			}
		}

		System.out.println("Debug %: " + (double) 100 * gc_count / total);
		return (float) 100 * gc_count / total;
	}
}