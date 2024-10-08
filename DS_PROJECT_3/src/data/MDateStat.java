package data;

import data_structs.linkedlist.LinkedList;
import data_structs.linkedlist.Node;
import data_structs.tree.TNode;

public class MDateStat implements Stat {
	
	private MDate date;
	private int totalMartyrs, totalFemales, totalMales;
	private int sumAges;
	private Martyr youngest, oldest;
	private String districtWithMaxMartyr, locationWithMaxMartyr, prevDistrict;
	
	private LinkedList<String> locations;
	
	private int currDistrictCount, maxDistrictCount;
	
	
	public MDateStat(MDate date) {
		this.date = date;
		locations = new LinkedList<>();
	}
	
	private void traverseMartyrs() {
		totalMartyrs = totalMales = totalFemales = sumAges = 0;
		locations.clear();
		locationWithMaxMartyr = "";
		districtWithMaxMartyr = "";
		currDistrictCount = maxDistrictCount = 0;
		TNode<Martyr> curr = date.getMartyrs().getRoot();
		if (curr == null) return;
		youngest = oldest = curr.getData();
		for (; curr.getLeft() != null; curr = curr.getLeft());
		prevDistrict = curr.getData().getDistrict();
		traverseMartyrs(date.getMartyrs().getRoot());
		if (maxDistrictCount < currDistrictCount) {
			System.out.println(prevDistrict);
			districtWithMaxMartyr = prevDistrict;
			maxDistrictCount = currDistrictCount;
		}
		findLocationWithMaxMartyr();
	}
	
	private void findLocationWithMaxMartyr() {
		Node<String> curr = locations.getHead();
		if (curr == null) return;
		locationWithMaxMartyr = curr.getData();
		int maxCount = 1, currCount = 1;
		while (curr.getNext() != null) {
			if (curr.getData().compareTo(curr.getNext().getData()) == 0) 
				currCount++;
			else {
				if (maxCount < currCount) {
					maxCount = currCount;
					locationWithMaxMartyr = curr.getData();
				}
				currCount = 1;
			}
			curr = curr.getNext();
		}
		if (maxCount < currCount) {
			maxCount = currCount;
			locationWithMaxMartyr = curr.getData();
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
		
		locations.insertSorted(m.getLocation());

		if (m.getDistrict().compareTo(prevDistrict) == 0) 
			currDistrictCount++;
		
		else {
			if (maxDistrictCount < currDistrictCount) {
				districtWithMaxMartyr = prevDistrict;
				maxDistrictCount = currDistrictCount;
			}
			currDistrictCount = 1;
		}
		prevDistrict = m.getDistrict();
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
