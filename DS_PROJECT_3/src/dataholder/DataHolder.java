package dataholder;

import data.District;
import data.MDate;
import data_structs.doublylinkedlist.DoublyLinkedList;
import data_structs.hash.Flag;
import data_structs.hash.HNode;
import data_structs.hash.QuadraticOHash;

public class DataHolder {
	
	private QuadraticOHash<MDate> dates;
	private MDate currentDate;
	private int currentIdx;
	
	private DoublyLinkedList<District> districts;

	
	public DataHolder() { }
	
	public DataHolder(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		updateDatesStat();
		if (dates.get(0).getFlag() == Flag.FULL) currentDate = dates.get(0).getData();
		else moveDown();
	}
	
	public QuadraticOHash<MDate> getDates() {
		return dates;
	}
	
	public void setDates(QuadraticOHash<MDate> dates) {
		this.dates = dates;
		updateDatesStat();
		if (dates.get(0).getFlag() == Flag.FULL) currentDate = dates.get(0).getData();
		else moveDown();
	}
	
	private void updateDatesStat() {
		int m = dates.getTableSize();
		for (int i = 0; i < m; i++) {
			HNode<MDate> date = dates.get(i);
			if (date.getFlag() == Flag.FULL)
				date.getData().getStat().updateStats();
		}
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
