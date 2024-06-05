package data;

import java.util.Date;

import data_structs.tree.AVLTree;

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

	public MDateStat getStat() {
		return stat;
	}

	public void setStat(MDateStat stat) {
		this.stat = stat;
	}
	
	@Override
	public int compareTo(MDate o) {
		return date.compareTo(o.date);
	}
	
	@Override
	public String toString() {
		// yyyy/mm/dd
		return String.format("%4d/%2d/%2d", date.getYear()+1900, date.getMonth()+1, date.getDate());
	}

	@Override
	public int hashCode() {
		return date.hashCode();
	}

}
