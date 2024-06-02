package doublylinkedlist;

public class DoublyLinkedList<T extends Comparable<T>> {

	private DNode<T> head;
	
	
	public DNode<T> getHead() {
		return head;
	}

	public void insert(T data) {
		DNode<T> newNode = new DNode<>(data), curr = head;

		for (; curr != null && curr.getData().compareTo(data) < 0 && curr.getNext() != null; curr = curr.getNext());
		
		if (curr == null) // case 0: empty list
			head = newNode;
		else if (curr.getData().compareTo(data) >= 0 && curr.getPrev() == null) { // case 1: insert at first
			newNode.setNext(curr);
			curr.setPrev(newNode);
			head = newNode;
		} else if (curr.getData().compareTo(data) < 0 && curr.getNext() == null) { // case 3: insert at last
			newNode.setPrev(curr);
			curr.setNext(newNode);
		} else { // case 2: insert between
			newNode.setNext(curr);
			newNode.setPrev(curr.getPrev());
			curr.getPrev().setNext(newNode);
			curr.setPrev(newNode);
		}
	}

	public DNode<T> delete(T data) {
		DNode<T> curr = head;

		for (; curr != null && curr.getData().compareTo(data) < 0;
				curr = curr.getNext());

		if (curr == null || curr.getData().compareTo(data) != 0)
			return null;

		if (curr.getPrev() == null && curr.getNext() == null) // delete 1 item
			head = null;
		else if (curr.getPrev() == null) { // delete the first item
			curr.getNext().setPrev(null);
			head = curr.getNext();
		}
		else if (curr.getNext() == null)  // delete the last item
			curr.getPrev().setNext(null);
		
		else {    // delete between                       
			curr.getNext().setPrev(curr.getPrev());
			curr.getPrev().setNext(curr.getNext());
		}
		return curr;
	}

	public DNode<T> find(T data) {
		DNode<T> curr = head;

		if (head == null)
			return null;

		for (; curr.getNext() != null && curr.getData().compareTo(data) < 0; curr = curr.getNext());

		if (curr.getData().compareTo(data) != 0)
			return null;

		return curr;
	}

	public int length() {
		DNode<T> curr = head;
		int count = 0;
		while (curr != null) {
			count++;
			curr = curr.getNext();
		}
		return count;
	}

	public void traverse() {
		DNode<T> curr = head;
		System.out.print("Head --> ");
		while (curr != null) {
			System.out.print(curr + " --> ");
			curr = curr.getNext();
		}
		System.out.println("Null");
	}

	@Override
	public String toString() {
		DNode<T> curr = head;
		String res = "Head --> ";
		while (curr != null) {
			res += curr + " --> ";
			curr = curr.getNext();
		}
		res += "Null";
		return res;
	}

}