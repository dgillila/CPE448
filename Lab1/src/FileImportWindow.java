import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileImportWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel = new JPanel(new BorderLayout());
	private JButton importButton = new JButton("Import");
	private JButton calculate = new JButton("Calculate");
	private JLabel results = new JLabel();
	private JTextField start = new JTextField(5);
	private JTextField stop = new JTextField(5);
	private String dna = "";

	private double result = 0.0;
	
	public FileImportWindow() {
		this.setTitle("GC Analyzer - Version 1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 400));
		this.setVisible(true);

		build();

		this.pack();
	}

	private void build() {
		Box leftBox = Box.createVerticalBox();
		leftBox.add(importButton);
		leftBox.add(calculate);

		calculate.addActionListener(calcListener);
		importButton.addActionListener(importListener);
		
		JPanel resultsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		resultsPanel.setBorder(BorderFactory.createTitledBorder("Results"));
		
		Box options = Box.createHorizontalBox();
		
		options.add(new JLabel("Start Index: "));
		options.add(start);
				
		options.add(new JLabel("Stop Index: "));
		options.add(stop);
		
		resultsPanel.add(options);
		resultsPanel.add(results);

		contentPanel.add(leftBox, BorderLayout.NORTH);
		contentPanel.add(resultsPanel, BorderLayout.CENTER);

		this.add(contentPanel);
	}
	
	private JFrame getThis() {
		return this;
	}
	
	private void update() {
		
		DecimalFormat df = new DecimalFormat("#.##");
        String res = df.format(result);
		
		results.setText("GC Content:    " + res + "%");
	}

	ActionListener importListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"FASTA files (*.txt)", "txt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(getThis());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath();
				
				dna = FileReader.readFile(path);		        
			}
		}
	};
	
	ActionListener calcListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int startIndex = 0;
			int stopIndex = dna.length() - 1;
			
			try{
				startIndex = Integer.parseInt(start.getText());
			} catch (Exception ex) {
				
			}
			
			try {
				stopIndex = Integer.parseInt(stop.getText());
			} catch (Exception ex) {
				
			}
			
			try{
				result = DNAAnalyzer.GCContent(dna, startIndex, stopIndex);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(getThis(), "Parameter error");
			}
			
			update();
		}
	};

}
