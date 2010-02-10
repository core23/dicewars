package de.core23.dicewars;

import java.io.IOException;

import javax.swing.SwingUtilities;

import de.core23.dicewars.controller.GameController;
import de.core23.dicewars.helper.LanguageManager;

public class DiceWarsAppDelegate {
	public static void main(String args[]) {
		// Load Language File
		try {
			LanguageManager.load("de"); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// MacOS Properties
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Dice Wars"); //$NON-NLS-1$ //$NON-NLS-2$

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GameController();
			}
		});
	}
}
