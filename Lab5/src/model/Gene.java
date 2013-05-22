package model;

import java.util.ArrayList;
import java.util.List;

public class Gene {

	protected String name;
	protected List<Isoform> isoforms;
	
	public Gene() {
		isoforms = new ArrayList<Isoform>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public void addIsoform(Isoform i) {
		isoforms.add(i);
	}
	
	public List<Isoform> getIsoforms() {
		return isoforms;
	}
	
	public Isoform findIsoform(String isoformName) {
		for(Isoform i : isoforms) {
			if(i.getName().equals(isoformName)) {
				return i;
			}
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Gene) {
			Gene other = (Gene)o;
			if(other.getName().equals(this.name)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
