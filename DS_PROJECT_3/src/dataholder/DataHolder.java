package dataholder;

import data.District;
import data.MDate;
import data.Martyr;
import doublylinkedlist.DoublyLinkedList;
import hash.Flag;
import hash.HNode;
import hash.QuadraticOHash;

public class DataHolder {
	
	private QuadraticOHash<MDate> dates;
	private MDate currentDate;
	private int currentIdx;
	
	private DoublyLinkedList<District> districts;

	
	public DataHolder() { }
	
	public DataHolder(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		moveDown();
	}
	
	public QuadraticOHash<MDate> getDates() {
		return dates;
	}
	
	public void setDates(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		
		moveDown();
	}
	
	private void updateDatesStat() {
		
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

	public DoublyLinkedList<District> getDistricts() {
		return districts;
	}

	public void setDistricts(DoublyLinkedList<District> districts) {
		this.districts = districts;
	}
	
}
