package data_structs.hash;

public class HNode<T extends Comparable<T>> {
	
	private T data;
	private Flag flag;
	private int index;
	
	public HNode(T data) {
		setData(data);
		flag = Flag.EMPTY;
	}
	
	public HNode(int index) {
		setIndex(index);
		flag = Flag.EMPTY;
	}
	
	@Override
	public String toString() {
		return (data == null) ? "" : data.toString();
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
