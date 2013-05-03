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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.DNAUtil;

public class DNAApplication extends JFrame {

	private static final long serialVersionUID = 1L;

	protected JPanel contentPanel;
	protected JTextField gffFileNameField, fastaFileNameField;
	protected JTextArea resultsField;
	protected JButton gffBrowseButton, fastaBrowseButton, calculateButton, saveButton, quitButton;

	public DNAApplication() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Gene Content Analyzer");
		this.setMinimumSize(new Dimension(600, 400));
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		gffFileNameField = new JTextField(20);
		fastaFileNameField = new JTextField(20);
		resultsField = new JTextArea();
		resultsField.setEditable(false);
		gffBrowseButton = new JButton("Browse...");
		fastaBrowseButton = new JButton("Browse...");
		calculateButton = new JButton("Calculate");
		saveButton = new JButton("Save");
		quitButton = new JButton("Quit");

		build();
	}

	protected void build() {

		Box topBox = Box.createVerticalBox();
		JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(new JLabel("Select a GFF File: "));
		inputPanel.add(gffFileNameField);
		inputPanel.add(gffBrowseButton);
		
		JPanel inputPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel2.add(new JLabel("Select a FASTA File: "));
		inputPanel2.add(fastaFileNameField);
		inputPanel2.add(fastaBrowseButton);
		
		topBox.add(inputPanel);
		topBox.add(inputPanel2);
		
		JPanel resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		resultsPanel.add(new JLabel("Results: "));
		
		topBox.add(resultsPanel);
		
		contentPanel.add(topBox);

		// JPanel resultsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

//		contentPanel.add(resultsPanel);

		JPanel scrollPanel = new JPanel(new BorderLayout());
		JScrollPane scrollDisplay = new JScrollPane(resultsField);
//		scrollDisplay.setPreferredSize(new Dimension(200, 1000));

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
	}

	protected void addActionListeners() {
		gffBrowseButton.addActionListener(browseListener);
		fastaBrowseButton.addActionListener(browseListener);
		quitButton.addActionListener(quitListener);
		saveButton.addActionListener(saveListener);
		calculateButton.addActionListener(calcListener);
	}

	protected void quit() {
		this.dispose();
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
				if(e.getSource() == gffBrowseButton) {
					gffFileNameField.setText(selectedFile.getAbsolutePath());
				} else if (e.getSource() == fastaBrowseButton) {
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

			String gffFilepath = gffFileNameField.getText();
			String fastaFilepath = fastaFileNameField.getText();

			try {
				resultsField.setText(DNAUtil.calculateResults(gffFilepath, fastaFilepath));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.toString(),
						"Error while reading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

}
