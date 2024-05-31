package dataholder;

import data.MDate;
import hash.QuadraticOHash;

public class DataHolder {
	
	private QuadraticOHash<MDate> dates;
	private MDate currentDate;
	private int currentIdx;

	
	public DataHolder(QuadraticOHash<MDate> dates) {
		this.dates = dates;
	}
	
	public void moveUp() {
		
	}
	
}
