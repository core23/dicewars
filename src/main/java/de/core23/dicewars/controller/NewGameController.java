package de.core23.dicewars.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.core23.dicewars.gui.NewGameDialog;
import de.core23.dicewars.helper.LanguageManager;
import de.core23.dicewars.misc.Actions;
import de.core23.dicewars.misc.Game;

public class NewGameController implements ActionListener, ItemListener {
	private NewGameDialog _frame;

	private GameController _gameController;

	public NewGameController(GameController gameController) {
		_gameController = gameController;
		_frame = new NewGameDialog();

		_frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == 10)
					start();
			}
		});

		for (int i = 0; i <= Game.PLAYER_MAX; i++)
			_frame.getChoicePlayer().add(String.valueOf(i));
		_frame.getChoicePlayer().select(_gameController.getNumPlayer());

		for (int i = 0; i <= Game.PLAYER_MAX; i++)
			_frame.getChoiceCPU().add(String.valueOf(i));
		_frame.getChoiceCPU().select(_gameController.getNumCPU());

		_frame.getChoiceCountries().add("16"); //$NON-NLS-1$
		_frame.getChoiceCountries().add("24"); //$NON-NLS-1$
		_frame.getChoiceCountries().add("32"); //$NON-NLS-1$
		_frame.getChoiceCountries().add("40"); //$NON-NLS-1$
		_frame.getChoiceCountries().add("48"); //$NON-NLS-1$
		_frame.getChoiceCountries().select(String.valueOf(_gameController.getNumCountrys()));

		_frame.getChoicePlayer().addItemListener(this);
		_frame.getChoiceCPU().addItemListener(this);
		_frame.getJButtonNewGame().addActionListener(this);

		_frame.setModal(true);
		_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void showWindow(Window window) {
		_frame.setLocationRelativeTo(window);
		_frame.setVisible(true);
		_frame.toFront();
		_frame.requestFocus();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(Actions.NEW_GAME)) {
			start();
		}
	}

	public void start() {
		try {
			int numPlayer = _frame.getChoicePlayer().getSelectedIndex();
			int numCPU = _frame.getChoiceCPU().getSelectedIndex();
			int numCountries = Integer.valueOf(_frame.getChoiceCountries().getSelectedItem());

			if (numCPU + numPlayer < Game.PLAYER_MIN)
				throw new SettingsException(String.format(LanguageManager.getString("settings.min.player.exception"), Game.PLAYER_MIN)); //$NON-NLS-1$
			if (numCPU + numPlayer > Game.PLAYER_MAX)
				throw new SettingsException(String.format(LanguageManager.getString("settings.max.player.exception"), Game.PLAYER_MAX)); //$NON-NLS-1$

			_gameController.newGame(numPlayer, numCPU, numCountries);
			_frame.dispose();

		} catch (SettingsException se) {
			JOptionPane.showMessageDialog(null, se.getMessage(), "DiceWars", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}

	public void itemStateChanged(ItemEvent ie) {
		int player = _frame.getChoicePlayer().getSelectedIndex();
		int cpu = _frame.getChoiceCPU().getSelectedIndex();

		if (ie.getSource() == _frame.getChoicePlayer()) {
			if (player + cpu > Game.PLAYER_MAX)
				_frame.getChoiceCPU().select(Game.PLAYER_MAX - player);

			if (player + cpu < Game.PLAYER_MIN)
				_frame.getChoiceCPU().select(Game.PLAYER_MIN - player);
		} else {
			if (player + cpu > Game.PLAYER_MAX)
				_frame.getChoicePlayer().select(Game.PLAYER_MAX - cpu);

			if (player + cpu < Game.PLAYER_MIN)
				_frame.getChoicePlayer().select(Game.PLAYER_MIN - cpu);
		}
	}
}
