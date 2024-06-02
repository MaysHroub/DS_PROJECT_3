package dataholder;

import data.District;
import data.MDate;
import doublylinkedlist.DoublyLinkedList;
import hash.Flag;
import hash.QuadraticOHash;

public class DataHolder {
	
	private QuadraticOHash<MDate> dates;
	private MDate currentDate;
	private int currentIdx;
	
	private DoublyLinkedList<District> districts;

	
	public DataHolder(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		moveDown();
	}
	
	public QuadraticOHash<MDate> getDates() {
		return dates;
	}
	
	public void moveUp() {
		int i = currentIdx;
		for (; --i >= 0 && dates.get(i).getFlag() != Flag.FULL;);
		if (i >= 0 && dates.get(i).getFlag() == Flag.FULL) {
			currentIdx = i;
			currentDate = dates.get(i).getData();
		}
	}
	
	public void moveDown() {
		int i = currentIdx, m = dates.getTableSize();
		for (; ++i < m && dates.get(i).getFlag() != Flag.FULL;);
		if (i < m && dates.get(i).getFlag() == Flag.FULL) {
			currentIdx = i;
			currentDate = dates.get(i).getData();
		}
	}

	public MDate getCurrentDate() {
		return currentDate;
	}
	
}
