package de.core23.dicewars.controller;

import java.awt.Window;

import javax.swing.JFrame;

import de.core23.dicewars.gui.AboutDialog;

public class AboutController {
	private AboutDialog _aboutDialog = null;

	public AboutController(GameController gameController) {
		_aboutDialog = new AboutDialog();
		_aboutDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		_aboutDialog.setModal(true);		
	}

	public void showWindow(Window window) {
		_aboutDialog.setLocationRelativeTo(window);
		_aboutDialog.setVisible(true);
		_aboutDialog.toFront();
		_aboutDialog.requestFocus();
	}
}
