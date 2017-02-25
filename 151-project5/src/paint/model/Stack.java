package paint.model;

public interface Stack<E> {
	
	public int size();
	
	public E pop();
	
	public void push(E e);
	
	public E peek();
	
	default public boolean isEmpty() {
		return size() == 0;
	}
	
	// Assertion
	default public void notEmpty() {
		if (isEmpty()) {
			throw new IllegalStateException("Cannot pop empty stack");
		}
	}
}
