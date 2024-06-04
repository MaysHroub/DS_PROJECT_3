package data;

import linkedlist.LinkedList;
import linkedlist.Node;
import tree.TNode;

public class MDateStat implements Stat {
	
	private MDate date;
	private int totalMartyrs, totalFemales, totalMales;
	private int sumAges;
	private Martyr youngest, oldest;
	private String DistrictWithMaxMartyr, LocationWithMaxMartyr;
	
	private LinkedList<String> districts, locations;
	
	
	public MDateStat(MDate date) {
		this.date = date;
		districts = locations = new LinkedList<>();
	}
	
	private void traverseMartyrs() {
		totalMartyrs = totalMales = totalFemales = sumAges = 0;
		youngest = oldest = date.getMartyrs().getRoot().getData();
		districts.clear(); locations.clear();
		traverseMartyrs(date.getMartyrs().getRoot());
		findDistrictWithMaxMartyr();
		findLocationWithMaxMartyr();
	}

	private void findDistrictWithMaxMartyr() {
		Node<String> curr = districts.getHead();
		if (curr == null) return;
		DistrictWithMaxMartyr = curr.getData();
		int maxCount = 1, currCount = 1;
		while (curr.getNext() != null) {
			if (curr.getData().compareTo(curr.getNext().getData()) == 0) 
				currCount++;
			else if (maxCount < currCount) {
				maxCount = currCount;
				DistrictWithMaxMartyr = curr.getData();
			}
			curr = curr.getNext();
		}
	}
	
	private void findLocationWithMaxMartyr() {
		Node<String> curr = locations.getHead();
		if (curr == null) return;
		LocationWithMaxMartyr = curr.getData();
		int maxCount = 1, currCount = 1;
		while (curr.getNext() != null) {
			if (curr.getData().compareTo(curr.getNext().getData()) == 0) 
				currCount++;
			else if (maxCount < currCount) {
				maxCount = currCount;
				LocationWithMaxMartyr = curr.getData();
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
		return DistrictWithMaxMartyr;
	}

	public String getLocationWithMaxMartyr() {
		return LocationWithMaxMartyr;
	}

	@Override
	public void updateStats() {
		traverseMartyrs();
	}
	
}
