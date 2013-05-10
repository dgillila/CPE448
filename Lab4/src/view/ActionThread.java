package view;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import model.Options;

import utils.DNAUtil;

public class ActionThread extends SwingWorker<String, Object> {

	private String gffFilepath, fastaFilepath;
	private JTextArea resultArea;
	private LoadingDialog ld;
	
	private Options options;

	@Override
	protected String doInBackground() throws Exception {
		return DNAUtil.calculateResults(options);
	}
	
	@Override
	protected void done() {
		try {
			resultArea.setText(get());
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error while performing calculations", JOptionPane.ERROR_MESSAGE);		
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
	
	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

}
