package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Options;

public class DNAApplication extends JFrame {

	private static final long serialVersionUID = 1L;

	protected JPanel contentPanel;
	protected JTextField fastaFileNameField;
	protected JTextField minLoopSizeField, maxLoopSizeField, minPalindromeSize, maxPalindromeSize;
	protected JTextArea resultsField;
	protected JButton fastaBrowseButton, calculateButton, saveButton, quitButton;
	protected JCheckBox allowUGPairs;


	public DNAApplication() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Palindrome Finder");
		this.setMinimumSize(new Dimension(700, 500));
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		fastaFileNameField = new JTextField(20);
		resultsField = new JTextArea();
		resultsField.setEditable(false);
		fastaBrowseButton = new JButton("Browse...");
		calculateButton = new JButton("Calculate");
		saveButton = new JButton("Save");
		quitButton = new JButton("Quit");
		minLoopSizeField = new JTextField(5);
		maxLoopSizeField = new JTextField(5);
		minPalindromeSize = new JTextField(5);
		maxPalindromeSize = new JTextField(5);
		allowUGPairs = new JCheckBox("Allow U-G Pairs");

		build();
	}

	protected void build() {

		Box topBox = Box.createVerticalBox();

		JPanel inputPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel2.add(new JLabel("Select a FASTA File: "));
		inputPanel2.add(fastaFileNameField);
		inputPanel2.add(fastaBrowseButton);

		JPanel inputPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel3.add(new JLabel("Minimum Loop Size: "));
		inputPanel3.add(minLoopSizeField);
		inputPanel3.add(new JLabel("Maximum Loop Size: "));
		inputPanel3.add(maxLoopSizeField);
		
		JPanel inputPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel4.add(new JLabel("Minimum Palindrome Size (Threshold): "));
		inputPanel4.add(minPalindromeSize);
		inputPanel4.add(new JLabel("Maximum Palindrome Size(Threshold): "));
		inputPanel4.add(maxPalindromeSize);

		JPanel inputPanel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel5.add(allowUGPairs);
		
		topBox.add(inputPanel2);
		topBox.add(inputPanel3);
		topBox.add(inputPanel4);
		topBox.add(inputPanel5);

		JPanel resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		resultsPanel.add(new JLabel("Results: "));

		topBox.add(resultsPanel);

		contentPanel.add(topBox);

		JPanel scrollPanel = new JPanel(new BorderLayout());
		JScrollPane scrollDisplay = new JScrollPane(resultsField);

		scrollPanel.add(scrollDisplay, BorderLayout.CENTER);

		contentPanel.add(scrollPanel);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(calculateButton);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(quitButton);
		contentPanel.add(buttonsPanel);

		addActionListeners();

		this.add(contentPanel);
		this.pack();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}


	protected void addActionListeners() {
		fastaBrowseButton.addActionListener(browseListener);
		quitButton.addActionListener(quitListener);
		saveButton.addActionListener(saveListener);
		calculateButton.addActionListener(calcListener);
	}

	protected void quit() {
		this.dispose();
	}

	protected JFrame getThis() {
		return this;
	}

	ActionListener browseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			// chooser.addChoosableFileFilter(new
			// FileNameExtensionFilter("FASTA and GFF files", "*"));

			int returnVal = chooser.showOpenDialog(chooser);

			if (returnVal == JFileChooser.CANCEL_OPTION) {
				System.out.println("cancelled");
			}

			else if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				if (e.getSource() == fastaBrowseButton) {
					fastaFileNameField.setText(selectedFile.getAbsolutePath());
				} else {
					System.out.println("Oops");
				}
			}

			else {
				System.out.println("Encountered Unknown Error");
				System.exit(0);
			}
		}
	};

	ActionListener quitListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			quit();
		}
	};

	ActionListener saveListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (resultsField.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "No output to save",
						"Empty output", JOptionPane.ERROR_MESSAGE);
			}

			else {
				JFileChooser chooser = new JFileChooser();

				int returnVal = chooser.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter writer = new FileWriter(
								chooser.getSelectedFile());
						writer.write(resultsField.getText());
						writer.close();
					} catch (java.io.IOException ioErr) {
						JOptionPane.showMessageDialog(null,
								"Encountered unknown error when saving output",
								"Unable to save output",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (returnVal == JFileChooser.ERROR_OPTION) {
					JOptionPane.showMessageDialog(null,
							"Encountered unknown error when saving output",
							"Unable to save output", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	};

	ActionListener calcListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			String fastaFilepath = fastaFileNameField.getText();

			Options o = new Options();
			o.fastaPath = fastaFilepath;
			o.allowUGPairs = allowUGPairs.isSelected();
			
			//Assigning all the values/default values
			try {
				o.minLoopSize = Integer.parseInt(minLoopSizeField.getText());
			} catch(Exception ex) { }
			try {
				o.maxLoopSize = Integer.parseInt(maxLoopSizeField.getText());
			} catch(Exception ex) { }
			try {
				o.minPalindromeSize = Integer.parseInt(minPalindromeSize.getText());
			} catch(Exception ex) { }
			try {
				o.maxPalindromeSize = Integer.parseInt(maxPalindromeSize.getText());
			} catch(Exception ex) { }
			
			if(o.minLoopSize > o.maxLoopSize || o.minPalindromeSize > o.maxPalindromeSize) {
				JOptionPane.showMessageDialog(getThis(), "Minimum cannot be greater than maximum", "Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			ActionThread thread = new ActionThread();
			thread.setOptions(o);
			thread.setResultArea(resultsField);

			LoadingDialog ld = new LoadingDialog();
			ld.showLoadingWindow(getThis());

			thread.setLd(ld);

			try {
				thread.execute();
			} catch (Exception ex) {
				System.out.println("thread exception");
			}

		}
	};
}
