package io;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

public class ClipboardListener extends Thread implements ClipboardOwner {
	private Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	private ClipboardBuffer buffer = new ClipboardBuffer();

	@Override
	public void run() {
		Transferable trans = sysClip.getContents(this);
		takeOwnership(trans);
		try {
			freeze();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void freeze() throws InterruptedException {
		Object obj = new Object();
		synchronized (obj) {
			obj.wait();
		}
	}

	@Override
	public void lostOwnership(Clipboard c, Transferable t) {
		try {
			ClipboardListener.sleep(250);  //waiting e.g for loading huge elements like word's etc.
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
		Transferable contents = sysClip.getContents(this);
		try {
			processClipboard(contents, c);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		takeOwnership(contents);
	}

	private void takeOwnership(Transferable t) {
		sysClip.setContents(t, this);
	}

	private void processClipboard(Transferable t, Clipboard c) { //your implementation
		try {
			if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				System.out.println((String) t.getTransferData(DataFlavor.stringFlavor));
				buffer.add(t);
			}
		} catch (Exception ignored) {
		}
	}

	public static void type(String characters) throws AWTException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection( characters );
		clipboard.setContents(stringSelection, null);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);

//		jTree1.addKeyListener(new java.awt.event.KeyAdapter() {
//
//			public void keyPressed(java.awt.event.KeyEvent evt) {
//				if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_C) {
//
//					JOptionPane.showMessageDialog(this, "ctrl + c");
//
//				} else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_X) {
//
//					JOptionPane.showMessageDialog(this, "ctrl + x");
//
//				} else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) {
//
//					JOptionPane.showMessageDialog(this, "ctrl + v");
//
//				}
//			}
//		});
	}

}
