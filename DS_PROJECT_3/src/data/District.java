package data;

import data_structs.linkedlist.LinkedList;

public class District implements Comparable<District> {
	
	private String name;
	private LinkedList<String> locations;
	
	public District(String name) {
		this.name = name;
		locations = new LinkedList<>();
	}
	
	public LinkedList<String> getLocations() {
		return locations;
	}
	
	public void insertLocation(String locationName) {
		if (locations.find(locationName) == null)
			locations.insertSorted(locationName);
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
