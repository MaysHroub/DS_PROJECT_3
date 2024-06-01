package data;

import linkedlist.LinkedList;

public class District {
	
	private String name;
	private LinkedList<String> locations;
	
	public District(String name) {
		this.name = name;
		locations = new LinkedList<>();
	}

}
