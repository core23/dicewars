package de.core23.dicewars.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.Choice;
import javax.swing.JButton;

import de.core23.dicewars.helper.LanguageManager;
import de.core23.dicewars.misc.Actions;

public class NewGameDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JPanel _jContentPane = null;

	private Label _labelPlayer = null;

	private Label _labelCPU = null;

	private Choice _choicePlayer = null;

	private Choice _choiceCPU = null;

	private JButton _jButtonNewGame = null;

	private Label _labelCountrys = null;

	private Choice _choiceCountries = null;

	public NewGameDialog() {
		super();

		initialize();
	}

	private void initialize() {
		this.setSize(220, 220);
		this.setContentPane(getJContentPane());
		this.setTitle(LanguageManager.getString("settings.newgame")); //$NON-NLS-1$
		this.setResizable(false);
	}

	public JPanel getJContentPane() {
		if (_jContentPane == null) {
			_labelCountrys = new Label();
			_labelCountrys.setBounds(new Rectangle(40, 100, 50, 20));
			_labelCountrys.setText(LanguageManager.getString("name.countries")); //$NON-NLS-1$
			_labelCPU = new Label();
			_labelCPU.setBounds(new Rectangle(40, 60, 50, 20));
			_labelCPU.setText(LanguageManager.getString("name.cpu")); //$NON-NLS-1$
			_labelPlayer = new Label();
			_labelPlayer.setBounds(new Rectangle(40, 20, 50, 20));
			_labelPlayer.setText(LanguageManager.getString("name.player")); //$NON-NLS-1$
			_jContentPane = new JPanel();
			_jContentPane.setLayout(null);
			_jContentPane.add(_labelPlayer, null);
			_jContentPane.add(_labelCPU, null);
			_jContentPane.add(getChoicePlayer(), null);
			_jContentPane.add(getChoiceCPU(), null);
			_jContentPane.add(getJButtonNewGame(), null);
			_jContentPane.add(_labelCountrys, null);
			_jContentPane.add(getChoiceCountries(), null);
		}
		return _jContentPane;
	}

	public Choice getChoicePlayer() {
		if (_choicePlayer == null) {
			_choicePlayer = new Choice();
			_choicePlayer.setBounds(new Rectangle(120, 20, 50, 20));
		}
		return _choicePlayer;
	}

	public Choice getChoiceCPU() {
		if (_choiceCPU == null) {
			_choiceCPU = new Choice();
			_choiceCPU.setBounds(new Rectangle(120, 60, 50, 20));
		}
		return _choiceCPU;
	}

	public JButton getJButtonNewGame() {
		if (_jButtonNewGame == null) {
			_jButtonNewGame = new JButton();
			_jButtonNewGame.setBounds(new Rectangle(40, 140, 130, 20));
			_jButtonNewGame.setText(LanguageManager.getString("name.start")); //$NON-NLS-1$
			_jButtonNewGame.setActionCommand(Actions.NEW_GAME);
		}
		return _jButtonNewGame;
	}

	public Choice getChoiceCountries() {
		if (_choiceCountries == null) {
			_choiceCountries = new Choice();
			_choiceCountries.setBounds(new Rectangle(120, 100, 50, 20));
		}
		return _choiceCountries;
	}
}
