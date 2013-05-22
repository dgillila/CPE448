package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Isoform {

	protected List<CDS> cdsRegions;
	protected String name;
	protected String chromosomeName;
	protected String source;
	protected String feature;
	protected int start;
	protected int stop;
	protected double score;
	protected boolean forwardStrand;
	protected int frame;
	protected Map<String, String> attributes;

	// POJO constructor
	public Isoform() {
		cdsRegions = new ArrayList<CDS>();
	}

	// GETTERS AND SETTERS
	public List<CDS> getCDSRegions() {
		return this.cdsRegions;
	}
	
	public void addCDS(CDS c) {
		this.cdsRegions.add(c);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChromosomeName() {
		return chromosomeName;
	}

	public void setChromosomeName(String chromosomeName) {
		this.chromosomeName = chromosomeName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isForwardStrand() {
		return forwardStrand;
	}

	public void setForwardStrand(boolean forwardStrand) {
		this.forwardStrand = forwardStrand;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(String key, String value) {
		if (attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(key, value);
	}

}
