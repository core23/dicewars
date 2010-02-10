package de.core23.dicewars.gui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.core23.dicewars.helper.LanguageManager;
import de.core23.dicewars.misc.Actions;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel _jContentPane = null;

	private GamePanel _gamePanel = null;

	private JButton _jButtonEndTurn = null;

	private JMenuBar _jJMenuBar = null;

	private JMenu _jMenuFile = null;

	private JMenuItem _jMenuItemExit = null;

	private JMenuItem _jMenuItemNewGame = null;

	private JMenuItem _jMenuItemAbout = null;

	private JMenu _jMenuHelp = null;

	private JTextArea _jTextAreaLog = null;

	private JScrollPane _jScrollPane = null;

	public GameFrame() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(890, 660);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Dice Wars"); //$NON-NLS-1$
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"))); //$NON-NLS-1$
	}

	public JPanel getJContentPane() {
		if (_jContentPane == null) {
			_jContentPane = new JPanel();
			_jContentPane.setLayout(null);
			_jContentPane.add(getGamePanel(), null);
			_jContentPane.add(getJButtonEndTurn(), null);
			_jContentPane.setBackground(Color.LIGHT_GRAY);
			_jContentPane.add(getJScrollPane());
		}
		return _jContentPane;
	}

	public JScrollPane getJScrollPane() {
		if (_jScrollPane == null) {
			_jScrollPane = new JScrollPane(getJTextAreaLog(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			_jScrollPane.setBounds(150, 10, 710, 60);
		}
		return _jScrollPane;
	}

	public GamePanel getGamePanel() {
		if (_gamePanel == null) {
			_gamePanel = new GamePanel();
			_gamePanel.setBounds(new Rectangle(0, 80, getWidth(), getHeight()));
		}
		return _gamePanel;
	}

	public JButton getJButtonEndTurn() {
		if (_jButtonEndTurn == null) {
			_jButtonEndTurn = new JButton();
			_jButtonEndTurn.setBounds(new Rectangle(25, 10, 120, 60));
			_jButtonEndTurn.setText(LanguageManager.getString("menu.end.turn")); //$NON-NLS-1$
			_jButtonEndTurn.setFocusable(false);
			_jButtonEndTurn.setActionCommand(Actions.END_TURN);
		}
		return _jButtonEndTurn;
	}

	public JMenuBar getJJMenuBar() {
		if (_jJMenuBar == null) {
			_jJMenuBar = new JMenuBar();
			_jJMenuBar.add(getJMenuFile());
			_jJMenuBar.add(getJMenuHelp());
		}
		return _jJMenuBar;
	}

	public JMenu getJMenuFile() {
		if (_jMenuFile == null) {
			_jMenuFile = new JMenu();
			_jMenuFile.setText(LanguageManager.getString("menu.file")); //$NON-NLS-1$
			_jMenuFile.setMnemonic('D');
			_jMenuFile.add(getJMenuItemNewGame());
			_jMenuFile.add(getJMenuItemExit());
		}
		return _jMenuFile;
	}

	public JMenuItem getJMenuItemNewGame() {
		if (_jMenuItemNewGame == null) {
			_jMenuItemNewGame = new JMenuItem();
			_jMenuItemNewGame.setText(LanguageManager.getString("menu.new.game")); //$NON-NLS-1$
			_jMenuItemNewGame.setMnemonic('N');
			_jMenuItemNewGame.setActionCommand(Actions.NEW_GAME);
		}
		return _jMenuItemNewGame;
	}

	public JMenu getJMenuHelp() {
		if (_jMenuHelp == null) {
			_jMenuHelp = new JMenu();
			_jMenuHelp.setText(LanguageManager.getString("menu.help")); //$NON-NLS-1$
			_jMenuHelp.setMnemonic('H');
			_jMenuHelp.add(getJMenuItemAbout());
		}
		return _jMenuHelp;
	}

	public JMenuItem getJMenuItemAbout() {
		if (_jMenuItemAbout == null) {
			_jMenuItemAbout = new JMenuItem();
			_jMenuItemAbout.setText(LanguageManager.getString("menu.info")); //$NON-NLS-1$
			_jMenuItemAbout.setMnemonic('I');
			_jMenuItemAbout.setActionCommand(Actions.INFO);
		}
		return _jMenuItemAbout;
	}

	public JMenuItem getJMenuItemExit() {
		if (_jMenuItemExit == null) {
			_jMenuItemExit = new JMenuItem();
			_jMenuItemExit.setText(LanguageManager.getString("menu.exit")); //$NON-NLS-1$
			_jMenuItemExit.setMnemonic('E');
			_jMenuItemExit.setActionCommand(Actions.EXIT);
		}
		return _jMenuItemExit;
	}

	public JTextArea getJTextAreaLog() {
		if (_jTextAreaLog == null) {
			_jTextAreaLog = new JTextArea(""); //$NON-NLS-1$
			_jTextAreaLog.setEditable(false);
			_jTextAreaLog.setFocusable(false);
			_jTextAreaLog.setFont(new Font("Helvetica", Font.PLAIN, 11)); //$NON-NLS-1$
			_jTextAreaLog.setMargin(new Insets(5, 5, 5, 5));
		}
		return _jTextAreaLog;
	}
}
