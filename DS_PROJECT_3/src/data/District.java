package data;

import linkedlist.LinkedList;

public class District implements Comparable<District> {
	
	private String name;
	private LinkedList<String> locations;
	
	public District(String name) {
		this.name = name;
		locations = new LinkedList<>();
	}

	@Override
	public int compareTo(District o) {
		return name.compareToIgnoreCase(o.name);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
