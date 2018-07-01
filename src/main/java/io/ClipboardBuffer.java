package io;

import java.util.Stack;

public class ClipboardBuffer<T> extends Stack<T> {
	private int maxSize;

	ClipboardBuffer(int size) {
		super();
		this.maxSize = size;
	}

	@Override
	public T push(T object) {
		if(this.size() > maxSize) {
			this.remove(0);
		}
		return super.push(object);
	}
}
