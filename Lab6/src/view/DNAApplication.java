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

import utils.DNAConcat;

public class DNAApplication extends JFrame {

	private static final long serialVersionUID = 1L;

	protected JPanel contentPanel;
	protected JTextField fastaNameField, gffNameField;
	protected JTextArea resultsField;
	protected JButton browseButton, gffBrowseButton, calculateButton, saveButton, quitButton;

	public DNAApplication() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Supercontig builder");
		this.setMinimumSize(new Dimension(600, 400));
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		fastaNameField = new JTextField(20);
		gffNameField = new JTextField(20);
		resultsField = new JTextArea();
		resultsField.setEditable(false);
		browseButton = new JButton("Browse");
		gffBrowseButton = new JButton("Browse");
		calculateButton = new JButton("Calculate");
		saveButton = new JButton("Save");
		quitButton = new JButton("Quit");

		build();
	}

	protected void build() {

		Box topBox = Box.createVerticalBox();
		JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(new JLabel("Select FASTA zip: "));
		inputPanel.add(fastaNameField);
		inputPanel.add(browseButton);
		
		JPanel inputPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel2.add(new JLabel("Select GFF zip: "));
		inputPanel2.add(gffNameField);
		inputPanel2.add(gffBrowseButton);
		
		topBox.add(inputPanel);
		topBox.add(inputPanel2);
		
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
	}

	protected void addActionListeners() {
		browseButton.addActionListener(browseListener);
		gffBrowseButton.addActionListener(gffBrowseListener);
		quitButton.addActionListener(quitListener);
		saveButton.addActionListener(saveListener);
		calculateButton.addActionListener(calcListener);
	}

	protected void quit() {
		this.dispose();
	}
	
	ActionListener gffBrowseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();

			int returnVal = chooser.showOpenDialog(chooser);

			if (returnVal == JFileChooser.CANCEL_OPTION) {
				System.out.println("cancelled");
			}

			else if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				gffNameField.setText(selectedFile.getAbsolutePath());
			}

			else {
				System.out.println("Encountered Unknown Error");
				System.exit(0);
			}
		}
	};

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
				fastaNameField.setText(selectedFile.getAbsolutePath());
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

			String fastaFilepath = fastaNameField.getText();
			String gffFilepath = gffNameField.getText();

			if (fastaFilepath == null || fastaFilepath.length() == 0
					|| gffFilepath == null || gffFilepath.length() == 0) {
				JOptionPane.showMessageDialog(null, "No file selected",
						"Unable to read file", JOptionPane.ERROR_MESSAGE);

				return;
			}

			try {
				resultsField.setText(DNAConcat.processFiles(fastaFilepath, gffFilepath));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.toString(),
						"Error while reading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

}
