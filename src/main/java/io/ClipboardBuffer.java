package io;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class ClipboardBuffer {
	private static final List<Transferable> clipboarBuffer;
	static {
		clipboarBuffer = new ArrayList<>();
	}

	public void add(Transferable t){
		clipboarBuffer.add(t);
		print();
	}

	public void print(){
		clipboarBuffer.forEach(System.out::println);
	}
}
