package view;

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class LoadingDialog {

	private JDialog window;
	private JFrame parent;

	public void showLoadingWindow(JFrame p) {
		parent = p;
		window = new JDialog(parent);

		JProgressBar loadingBar = new JProgressBar();
		loadingBar.setIndeterminate(true);

		window.setTitle("Loading...");
		window.setVisible(true);
		window.add(loadingBar);
		window.pack();
		window.setLocationRelativeTo(parent);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		parent.setEnabled(false);
	}

	public void hideLoadingWindow() {
		parent.setEnabled(true);
		window.setVisible(false);
	}

}
