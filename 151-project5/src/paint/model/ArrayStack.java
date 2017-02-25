package paint.model;

public class ArrayStack<E> implements Stack<E> {
	
	private int top;
	private E[] stuff;
	
	public ArrayStack() {
		this(8);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayStack(int initialSize) {
		top = -1;
		stuff = (E[])(new Object[initialSize]);
	}
	
	@Override
	public int size() {
		return top + 1;
	}

	@Override
	public E pop() {
		notEmpty();
		E temp = stuff[top];
		top--;
		return temp;
	}

	@Override
	public E peek() {
		notEmpty();
		return stuff[top];
	}

	@Override
	public void push(E e) {
		resize();
		top++;
		stuff[top] = e;
	}
	
	@SuppressWarnings("unchecked")
	private void resize() {
		if (top == stuff.length - 1) {
			E[] stuff2 = (E[])(new Object[stuff.length * 2]);
			for (int i = 0; i < stuff.length; i++) {
				stuff2[i] = stuff[i];
			}
			stuff = stuff2;
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i <= top; i++) {
			s += stuff[i] + " ";
		}
		return s;
	}
}
