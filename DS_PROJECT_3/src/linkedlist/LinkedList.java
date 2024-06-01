package linkedlist;


public class LinkedList<T extends Comparable<T>> {

	private Node<T> head;
	
	
	public Node<T> getHead() {
		return head;
	}
	
	public void insertSorted(T data) {
		Node<T> newNode = new Node<>(data);
		
		if (head == null) {
			head = newNode;
			return;
		}
		Node<T> prev = null, curr = head;
		for (; curr != null && curr.getData().compareTo(data) < 0;
				prev = curr, curr = curr.getNext());
		if (prev == null /*or curr == head*/) {  // insert first
			newNode.setNext(head);
			head = newNode;
		}
		else if (curr == null)   // insert last
			prev.setNext(newNode);
		else {                   // insert between
			newNode.setNext(curr);
			prev.setNext(newNode);
		}
	}
	
	public Node<T> find(T data) {
		Node<T> curr = head;
		for (; curr != null && curr.getData().compareTo(data) < 0;
				curr = curr.getNext());
		if (curr != null && curr.getData().compareTo(data) == 0)
			return curr;
		return null;
	}
	
	public Node<T> delete(T data) {
		Node<T> prev = null, curr = head;
		for (; curr != null && curr.getData().compareTo(data) < 0;
				prev = curr, curr = curr.getNext());
		if (curr != null && curr.getData().compareTo(data) == 0) {
			if (prev == null)  // delete the first node
				head = curr.getNext();
			else                // delete a middle node or the last one
				prev.setNext(curr.getNext());
			return curr;
		}
		return null;
	}
	
	public int length() {
		int count = 0;
		Node<T> curr = head;
		while (curr != null) {
			count++;
			curr = curr.getNext();
		}
		return count;
	}
	
	public void clear() {
		head = null;
	}
	
	public void traverse() {
		Node<T> curr = head;
		System.out.print("Head --> ");
		while (curr != null) {
			System.out.print(curr + " --> ");
			curr = curr.getNext();
		}
		System.out.println("Null");
	}
	
	@Override
	public String toString() {
		Node<T> curr = head;
		String linkedlist = "Head --> ";
		while (curr != null) {
			linkedlist += curr + " --> ";
			curr = curr.getNext();
		}
		return linkedlist + "Null";
	}
	
	
}

