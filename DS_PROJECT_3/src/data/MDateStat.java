package data;

import linkedlist.LinkedList;
import linkedlist.Node;
import tree.TNode;

public class MDateStat implements Stat {
	
	private MDate date;
	private int totalMartyrs, totalFemales, totalMales;
	private int sumAges;
	private Martyr youngest, oldest;
	private String districtWithMaxMartyr, locationWithMaxMartyr;
	
	private LinkedList<String> districts, locations;
	
	
	public MDateStat(MDate date) {
		this.date = date;
		districts = locations = new LinkedList<>();
	}
	
	private void traverseMartyrs() {
		totalMartyrs = totalMales = totalFemales = sumAges = 0;
		districts.clear(); locations.clear();
		districtWithMaxMartyr = locationWithMaxMartyr = "";
		if (date.getMartyrs().getRoot() == null) return;
		youngest = oldest = date.getMartyrs().getRoot().getData();
		traverseMartyrs(date.getMartyrs().getRoot());
		findDistrictWithMaxMartyr();
		findLocationWithMaxMartyr();
	}

	private void findDistrictWithMaxMartyr() {
		Node<String> curr = districts.getHead();
		if (curr == null) return;
		districtWithMaxMartyr = curr.getData();
		int maxCount = 1, currCount = 1;
		while (curr.getNext() != null) {
			if (curr.getData().compareTo(curr.getNext().getData()) == 0) 
				currCount++;
			else if (maxCount < currCount) {
				maxCount = currCount;
				districtWithMaxMartyr = curr.getData();
			}
			curr = curr.getNext();
		}
	}
	
	private void findLocationWithMaxMartyr() {
		Node<String> curr = locations.getHead();
		if (curr == null) return;
		locationWithMaxMartyr = curr.getData();
		int maxCount = 1, currCount = 1;
		while (curr.getNext() != null) {
			if (curr.getData().compareTo(curr.getNext().getData()) == 0) 
				currCount++;
			else if (maxCount < currCount) {
				maxCount = currCount;
				locationWithMaxMartyr = curr.getData();
			}
			curr = curr.getNext();
		}
	}

	private void traverseMartyrs(TNode<Martyr> curr) {
		if (curr == null) return;
		traverseMartyrs(curr.getLeft());
		totalMartyrs++;
		Martyr m = curr.getData();
		if (m.getGender() == 'F') totalFemales++;
		else totalMales++;
		if (youngest.getAge() > m.getAge()) youngest = m;
		if (oldest.getAge() < m.getAge()) oldest = m;
		sumAges += m.getAge();
		districts.insertSorted(m.getDistrict());
		locations.insertSorted(m.getLocation());
		traverseMartyrs(curr.getRight());
	}

	public int getTotalMartyrs() {
		return totalMartyrs;
	}

	public int getTotalFemales() {
		return totalFemales;
	}

	public int getTotalMales() {
		return totalMales;
	}

	public double getAvgAges() {
		return (totalMartyrs != 0) ? sumAges / (double) totalMartyrs : 0;
	}

	public Martyr getYoungest() {
		return youngest;
	}

	public Martyr getOldest() {
		return oldest;
	}

	public String getDistrictWithMaxMartyr() {
		return districtWithMaxMartyr;
	}

	public String getLocationWithMaxMartyr() {
		return locationWithMaxMartyr;
	}

	@Override
	public void updateStats() {
		traverseMartyrs();
	}
	
}
