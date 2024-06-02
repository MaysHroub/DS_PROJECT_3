package data;

import java.util.Date;

import tree.AVLTree;

public class MDate implements Comparable<MDate> {
	
	private MDateStat stat;
	private Date date;
	private AVLTree<Martyr> martyrs;
	
	public MDate(Date date) {
		setDate(date);
		martyrs = new AVLTree<>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AVLTree<Martyr> getMartyrs() {
		return martyrs;
	}

	public void setMartyrs(AVLTree<Martyr> martyrs) {
		this.martyrs = martyrs;
	}

	@Override
	public int compareTo(MDate o) {
		return date.compareTo(o.date);
	}

}
