package view;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import utils.DNAUtil;

public class ActionThread extends SwingWorker<String, Object> {

	private String gffFilepath, fastaFilepath;
	private JTextArea resultArea;
	private LoadingDialog ld;
	
	@Override
	protected String doInBackground() throws Exception {
		return DNAUtil.calculateResults(gffFilepath, fastaFilepath);
	}
	
	@Override
	protected void done() {
		try {
			resultArea.setText(get());
		} catch(Exception e) {
			System.out.println("Exception");
		} finally {
			ld.hideLoadingWindow();
		}
	}

	public String getGffFilepath() {
		return gffFilepath;
	}

	public void setGffFilepath(String gffFilepath) {
		this.gffFilepath = gffFilepath;
	}

	public String getFastaFilepath() {
		return fastaFilepath;
	}

	public void setFastaFilepath(String fastaFilepath) {
		this.fastaFilepath = fastaFilepath;
	}

	public JTextArea getResultArea() {
		return resultArea;
	}

	public void setResultArea(JTextArea resultArea) {
		this.resultArea = resultArea;
	}

	public LoadingDialog getLd() {
		return ld;
	}

	public void setLd(LoadingDialog ld) {
		this.ld = ld;
	}

}
