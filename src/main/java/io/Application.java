package io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class Application {
	private static final ClipboardOwner owner = (clipboard, contents) -> System.out.println("SuperOwner");
	private static final Clipboard clipboard;

	static {
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public static void main(String[] args) {
		new ClipboardListener().start();
	}

	private static void stringToClipboard(String val){
		StringSelection selection = new StringSelection(val);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	private static void printStringFromClipboard() {
		try {
			if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
				System.out.println((String) clipboard.getData(DataFlavor.stringFlavor));
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private static String getStringFromClipboard() {
		try {
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void addListener(){
		clipboard.addFlavorListener(e -> {
			printStringFromClipboard();
			System.out.println(((MyClipboard)clipboard).haveSameOwner(owner));
			clipboard.setContents(clipboard.getContents(new Object()), owner);
		});
	}
}
