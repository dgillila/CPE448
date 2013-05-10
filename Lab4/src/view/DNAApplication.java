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
	protected JTextField gffFileNameField, fastaFileNameField;
	protected JTextArea resultsField;
	protected JButton gffBrowseButton, fastaBrowseButton, calculateButton,
			saveButton, quitButton;

	protected JPanel additionalOptionsPanel;
	protected JCheckBox showAdditionalOptions, toggleSearchType,
			toggleSpaceType;

	protected JTextField repeatSizeMinField, repeatSizeMaxField;
	protected JTextField searchStartField, searchStopField,
			filterEnrichmentThresholdField, specifiedSequencesField,
			upstreamSizeField;

	protected JPanel searchByStringPanel, searchBySizePanel;
	protected JPanel searchUpstreamPanel, searchSpacePanel;

	public DNAApplication() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Gene Content Analyzer");
		this.setMinimumSize(new Dimension(700, 500));
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

		additionalOptionsPanel = new JPanel();
		showAdditionalOptions = new JCheckBox("Show additional options");
		toggleSearchType = new JCheckBox("Search By Sequence");
		toggleSpaceType = new JCheckBox("Search Upstream");

		repeatSizeMinField = new JTextField(5);
		repeatSizeMaxField = new JTextField(5);

		searchStartField = new JTextField(5);
		searchStopField = new JTextField(5);
		upstreamSizeField = new JTextField(5);
		filterEnrichmentThresholdField = new JTextField(5);
		specifiedSequencesField = new JTextField(40);

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

		JPanel inputPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel3.add(showAdditionalOptions);

		buildAdditionalOptionsPanel();

		JPanel inputPanel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel4.add(additionalOptionsPanel);
		additionalOptionsPanel.setVisible(false);

		topBox.add(inputPanel);
		topBox.add(inputPanel2);
		topBox.add(inputPanel3);
		topBox.add(inputPanel4);

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

	protected void buildAdditionalOptionsPanel() {

		searchByStringPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchBySizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		searchBySizePanel.add(new JLabel("Minimum Repeat Size:"));
		searchBySizePanel.add(repeatSizeMinField);
		searchBySizePanel.add(new JLabel("Maximum Repeat Size:"));
		searchBySizePanel.add(repeatSizeMaxField);

		searchByStringPanel.add(new JLabel("DNA sequence:"));
		searchByStringPanel.add(specifiedSequencesField);

		searchByStringPanel.setVisible(false);

		JPanel toggleHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toggleHeader.add(toggleSearchType);

		JPanel searchSpaceHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchSpaceHeader.add(toggleSpaceType);
		searchSpacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchSpacePanel.add(new JLabel("Start Index"));
		searchSpacePanel.add(searchStartField);
		searchSpacePanel.add(new JLabel("Stop Index"));
		searchSpacePanel.add(searchStopField);

		searchUpstreamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchUpstreamPanel.add(new JLabel("Search Upstream Size:"));
		searchUpstreamPanel.add(upstreamSizeField);

		searchUpstreamPanel.setVisible(false);

		JPanel filterHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterHeader.add(new JLabel("Filter By Fold Enrichment Threshold:"));
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.add(new JLabel("Threshold Value:"));
		filterPanel.add(filterEnrichmentThresholdField);

		Box vFlow = Box.createVerticalBox();

		vFlow.add(toggleHeader);
		vFlow.add(searchBySizePanel);
		vFlow.add(searchByStringPanel);
		vFlow.add(searchSpaceHeader);
		vFlow.add(searchUpstreamPanel);
		vFlow.add(searchSpacePanel);
		vFlow.add(filterHeader);
		vFlow.add(filterPanel);

		additionalOptionsPanel.add(vFlow);
	}

	protected void addActionListeners() {
		gffBrowseButton.addActionListener(browseListener);
		fastaBrowseButton.addActionListener(browseListener);
		quitButton.addActionListener(quitListener);
		saveButton.addActionListener(saveListener);
		calculateButton.addActionListener(calcListener);
		showAdditionalOptions.addActionListener(toggleAdditionalOptions);
		toggleSearchType.addActionListener(toggleSearchTypeListener);
		toggleSpaceType.addActionListener(toggleSpaceTypeListener);
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
				if (e.getSource() == gffBrowseButton) {
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

			Options o = new Options();
			o.gffPath = gffFilepath;
			o.fastaPath = fastaFilepath;

			if (toggleSearchType.isSelected()) {
				System.out.println("By sequence");
				o.sequence = specifiedSequencesField.getText();
				o.bySize = false;
			} else {
				System.out.println("By size");
				o.bySize = true;
				try {
					o.minSize = Integer.parseInt(repeatSizeMinField.getText());
				} catch (Exception ex) {
				}
				try {
					o.maxSize = Integer.parseInt(repeatSizeMaxField.getText());
				} catch (Exception ex) {
				}
			}

			if (toggleSpaceType.isSelected()) {
				System.out.println("By upstream");
				o.byUpstream = true;
				try {
					o.upstreamSize = Integer.parseInt(upstreamSizeField.getText());
				} catch (Exception ex) {
				}
			} else {
				System.out.println("By range");
				o.byUpstream = false;
				try {
					o.start = Integer.parseInt(searchStartField.getText());
				} catch (Exception ex) {
				}
				try {
					o.stop = Integer.parseInt(searchStopField.getText());
				} catch (Exception ex) {
				}
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

	ActionListener toggleAdditionalOptions = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (showAdditionalOptions.isSelected()) {
				additionalOptionsPanel.setVisible(true);
			} else {
				additionalOptionsPanel.setVisible(false);
			}
		}
	};

	ActionListener toggleSearchTypeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (toggleSearchType.isSelected()) {
				searchBySizePanel.setVisible(false);
				searchByStringPanel.setVisible(true);
			} else {
				searchBySizePanel.setVisible(true);
				searchByStringPanel.setVisible(false);
			}
		}
	};

	ActionListener toggleSpaceTypeListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (toggleSpaceType.isSelected()) {
				searchSpacePanel.setVisible(false);
				searchUpstreamPanel.setVisible(true);
			} else {
				searchSpacePanel.setVisible(true);
				searchUpstreamPanel.setVisible(false);
			}
		}
	};

}
