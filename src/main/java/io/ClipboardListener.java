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
	private final Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	private final ClipboardBuffer<Transferable> buffer = new ClipboardBuffer<>(5);

	@Override
	public void run() {
		takeOwnership(sysClip.getContents(this));
		freeze();
	}

	private void freeze() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException("Execution interrupted");
			}
		}
	}

	@Override
	public void lostOwnership(Clipboard c, Transferable t) {
		try {
			ClipboardListener.sleep(200); //wait for large objects processed
			//TODO request dynamically
		} catch(Exception e) {
			e.printStackTrace();
		}

		/*if ownership is lost, save clipboard content and return ownership*/
		Transferable clipboardContent = sysClip.getContents(this);
		saveContentToBuffer(clipboardContent);
		takeOwnership(clipboardContent);
	}

	/*Intercept ownership on a system clipboart*/
	private void takeOwnership(Transferable t) {
		sysClip.setContents(t, this);
	}

	private void saveContentToBuffer(Transferable content){
		if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			buffer.push(content);
		}
	}

	private static void type(String toPaste) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(toPaste);
		clipboard.setContents(stringSelection, null);
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		//System.out.println((String) t.getTransferData(DataFlavor.stringFlavor));

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
