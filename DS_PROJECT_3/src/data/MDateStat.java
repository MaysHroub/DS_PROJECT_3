package data;

import tree.TNode;

public class MDateStat {
	
	private MDate date;
	private int totalMartyrs, totalFemales, totalMales;
	private int sumAges;
	private Martyr youngest, oldest;
	
	public MDateStat(MDate date) {
		this.date = date;
	}
	
	private void traverseMartyrs() {
		youngest = oldest = date.getMartyrs().getRoot().getData();
		traverseMartyrs(date.getMartyrs().getRoot());
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
		return sumAges;
	}

	public Martyr getYoungest() {
		return youngest;
	}

	public Martyr getOldest() {
		return oldest;
	}
	
}
