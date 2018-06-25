package io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;

public class MyClipboard extends Clipboard {
	/**
	 * Creates a clipboard object.
	 *
	 * @param name
	 * @see Toolkit#getSystemClipboard
	 */
	public MyClipboard(String name) {
		super(name);
	}

	public boolean haveSameOwner(ClipboardOwner owner){
		return this.owner == owner;
	}
}
