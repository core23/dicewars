package de.core23.dicewars.controller;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import de.core23.dicewars.gui.GameFrame;
import de.core23.dicewars.helper.LanguageManager;
import de.core23.dicewars.helper.MapGenerator;
import de.core23.dicewars.misc.Actions;
import de.core23.dicewars.misc.Game;
import de.core23.dicewars.model.Dice;
import de.core23.dicewars.model.Player;
import de.core23.dicewars.model.Region;

public class GameController implements KeyListener, ActionListener, MouseListener {
	private static Random RANDOM = new Random();

	private MapGenerator _mapGenerator;

	private GameFrame _frame;

	private boolean _throwDice = true;

	private LinkedList<Player> _playerList = new LinkedList<Player>();

	private Player _activePlayer;

	private LinkedList<Region> _regionList = new LinkedList<Region>();

	private Region _regionSelected = null;

	private Region _regionAttacked = null;

	private LinkedList<Dice> _diceList = new LinkedList<Dice>();

	private int _numCountries = 32;

	private int _numPlayer = 2;

	private int _numCPU = 0;

	private Timer _timer = null;

	public GameController() {
		_mapGenerator = new MapGenerator();

		_frame = new GameFrame();

		_frame.addKeyListener(this);
		_frame.getJButtonEndTurn().addActionListener(this);
		_frame.getJMenuItemExit().addActionListener(this);
		_frame.getJMenuItemAbout().addActionListener(this);
		_frame.getJMenuItemNewGame().addActionListener(this);
		_frame.getGamePanel().addMouseListener(this);

		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setLocationRelativeTo(null);
		_frame.setVisible(true);

		// Action Timer
		_timer = new Timer(150, this);
		_timer.setActionCommand(Actions.TIMER);
		_timer.start();

		newGame(1, 7, 32);
	}

	public void newGame() {
		NewGameController controller = new NewGameController(this);
		controller.showWindow(_frame);
	}

	public void newGame(int player, int cpu, int countries) {
		_numPlayer = player;
		_numCPU = cpu;
		_numCountries = countries;
		_throwDice = false;

		// Create Players
		_playerList.clear();
		for (int i = 0; i < _numPlayer + _numCPU; i++) {
			if ((player > 0 && RANDOM.nextBoolean()) || cpu == 0) {
				player--;
				_playerList.add(new Player(i, false));
			} else if (cpu > 0) {
				cpu--;
				_playerList.add(new Player(i, true));

			}
		}

		// Set Active Player
		_activePlayer = _playerList.getFirst();
		_frame.getJButtonEndTurn().setEnabled(!_activePlayer.isCPU());

		// Clear Map
		_regionList.clear();
		_regionList = _mapGenerator.generate(countries);

		// Set Owners
		for (int i = 0; i < _regionList.size(); i++)
			_regionList.get(i).setPlayer(_playerList.get(i % (_numPlayer + _numCPU)));

		// Set Dices
		int dices = (_regionList.size() / _playerList.size()) * (Game.DICE_START - 1);
		for (Player p : _playerList)
			placeDices(p, dices);

		// Pass 2 GUI
		clearDiceList();
		_frame.getGamePanel().setRegionList(_regionList);
		_frame.getGamePanel().setPlayerList(_playerList);
		setRegionAttacked(null);
		setRegionSelected(null);
		setActivePlayer(_activePlayer);

		// Log
		String text = ""; //$NON-NLS-1$
		if (_numCPU == 1)
			text += LanguageManager.getString("log.cpu.one"); //$NON-NLS-1$
		else if (_numCPU > 1)
			text += String.format(LanguageManager.getString("log.cpu.multi"), _numCPU); //$NON-NLS-1$
		if (!text.equals("")) //$NON-NLS-1$
			text += LanguageManager.getString("log.and"); //$NON-NLS-1$
		if (_numPlayer == 1)
			text += LanguageManager.getString("log.player.one"); //$NON-NLS-1$
		else if (_numPlayer > 1)
			text += String.format(LanguageManager.getString("log.player.multi"), _numPlayer); //$NON-NLS-1$

		printLog(String.format(LanguageManager.getString("log.game.start"), text)); //$NON-NLS-1$
	}

	private void setRegionSelected(Region regionSelected) {
		_regionSelected = regionSelected;
		_frame.getGamePanel().setRegionSelected(regionSelected);
	}

	private void setRegionAttacked(Region regionAttacked) {
		_regionAttacked = regionAttacked;
		_frame.getGamePanel().setRegionAttacked(regionAttacked);
	}

	private void clearDiceList() {
		_diceList.clear();
		_frame.getGamePanel().clearDiceList();
	}

	private void addDice(Region region) {
		Dice dice = new Dice(region);
		_diceList.add(dice);
		_frame.getGamePanel().addDice(dice);
	}

	private void setActivePlayer(Player player) {
		_activePlayer = player;
		_frame.getGamePanel().setActivePlayer(player);
	}

	private void endTurn() {
		if (_throwDice)
			return;

		clearDiceList();
		setRegionSelected(null);
		setRegionAttacked(null);

		placeDices(_activePlayer, _activePlayer.getMaxRegionCluster());

		int next = _playerList.indexOf(_activePlayer) + 1;
		if (next >= _playerList.size())
			next = 0;
		setActivePlayer(_playerList.get(next));

		_frame.getJButtonEndTurn().setEnabled(!_activePlayer.isCPU());
	}

	public void showInfo() {
		AboutController contoller = new AboutController(this);
		contoller.showWindow(_frame);
	}

	private void placeDices(Player player, int dices) {
		LinkedList<Region> possibleRegions = new LinkedList<Region>();
		for (Region region : player.getRegions()) {
			if (region.getDices() < Game.DICE_MAX)
				possibleRegions.add(region);
		}

		// Add Dices
		for (int i = 0; i < dices; i++) {
			if (possibleRegions.size() == 0)
				return;

			Region rndRegion = possibleRegions.get(RANDOM.nextInt(possibleRegions.size()));
			rndRegion.addDice();

			if (rndRegion.getDices() >= Game.DICE_MAX)
				possibleRegions.remove(rndRegion);
		}
	}

	private void switchRegion(int pos) {
		if (_throwDice)
			return;

		setRegionAttacked(null);

		// Create List
		LinkedList<Region> possibleRegions = new LinkedList<Region>();
		for (Region region : _activePlayer.getRegions()) {
			if (region.getDices() == 1)
				continue;
			if (region.getEnemyNeighbors().size() == 0)
				continue;
			possibleRegions.add(region);
		}

		if (possibleRegions.size() == 0)
			return;
		if (possibleRegions.size() == 1)
			setRegionSelected(possibleRegions.getFirst());

		if (_regionSelected == null) {
			setRegionSelected((pos > 0) ? possibleRegions.getFirst() : possibleRegions.getLast());
		} else {
			int index = possibleRegions.indexOf(_regionSelected) + pos;

			if (index < 0)
				index = possibleRegions.size() - 1;
			else if (index >= possibleRegions.size())
				index = 0;

			setRegionSelected(possibleRegions.get(index));
		}
	}

	private void switchAttack(int pos) {
		if (_throwDice)
			return;
		if (_regionSelected == null)
			return;
		if (_regionSelected.getDices() == 1)
			return;

		// Create List
		LinkedList<Region> possibleRegions = _regionSelected.getEnemyNeighbors();

		if (possibleRegions.size() == 0)
			return;
		if (possibleRegions.size() == 1)
			setRegionAttacked(possibleRegions.getFirst());

		if (_regionAttacked == null) {
			setRegionAttacked((pos > 0) ? possibleRegions.getFirst() : possibleRegions.getLast());
		} else {
			int index = possibleRegions.indexOf(_regionAttacked) + pos;

			if (index < 0)
				index = possibleRegions.size() - 1;
			else if (index >= possibleRegions.size())
				index = 0;

			setRegionAttacked(possibleRegions.get(index));
		}
	}

	private void throwDice() {
		int diceSelected = 0;
		int diceAttacked = 0;

		if (_regionSelected == null || _regionAttacked == null)
			return;

		for (Dice dice : _diceList) {
			if (dice.getRegion() == _regionSelected)
				diceSelected += dice.getValue();
			else
				diceAttacked += dice.getValue();
		}

		if (_diceList.size() == _regionSelected.getDices())
			printLog(String.format(LanguageManager.getString("log.throw.dice"), _regionSelected.getPlayerName(), diceSelected)); //$NON-NLS-1$

		if (_diceList.size() == _regionSelected.getDices() + _regionAttacked.getDices())
			printLog(String.format(LanguageManager.getString("log.throw.dice"), _regionAttacked.getPlayerName(), diceAttacked)); //$NON-NLS-1$

		// Throw Selected Dices
		if (_diceList.size() < _regionSelected.getDices()) {
			addDice(_regionSelected);

			// Throw Attack Dices
		} else if (_diceList.size() < _regionSelected.getDices() + _regionAttacked.getDices()) {
			addDice(_regionAttacked);

			// Attack
		} else {
			if (diceSelected > diceAttacked)
				printLog(String.format(LanguageManager.getString("log.throw.win"), _regionSelected.getPlayerName())); //$NON-NLS-1$
			else
				printLog(String.format(LanguageManager.getString("log.throw.lost"), _regionSelected.getPlayerName())); //$NON-NLS-1$

			// Move Dices
			if (diceSelected > diceAttacked) {
				if (_regionAttacked.getPlayer().getRegionSize() == 1)
					_playerList.remove(_regionAttacked.getPlayer());

				_regionAttacked.setPlayer(_regionSelected.getPlayer());
				_regionAttacked.setDices(_regionSelected.getDices() - 1);
			}
			_regionSelected.setDices(1);

			// Attack End
			setRegionSelected(null);
			setRegionAttacked(null);
			_throwDice = false;

			// Game Over
			if (_playerList.size() == 1) {
				endGame();
			}
		}
	}

	private void endGame() {
		_throwDice = true;

		_frame.getGamePanel().repaint();

		printLog(String.format(LanguageManager.getString("log.game.end"), _playerList.getFirst().getName())); //$NON-NLS-1$
		JOptionPane
			.showMessageDialog(
				_frame,
				String.format(LanguageManager.getString("game.end.message"), _playerList.getFirst().getName()), LanguageManager.getString("game.end.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private void cpuAction() {
		LinkedList<Region> possibleRegions = new LinkedList<Region>();
		possibleRegions.addAll(_activePlayer.getRegions());
		Collections.shuffle(possibleRegions);

		for (Region region : _activePlayer.getRegions()) {
			if (region.getDices() == 1)
				continue;

			// Create Enemy List
			LinkedList<Region> possibleEnemies = new LinkedList<Region>();
			for (Region neighbor : region.getEnemyNeighbors()) {
				if (region.getDices() * 6 < neighbor.getDices())
					continue;
				if (region.getDices() >= neighbor.getDices() || RANDOM.nextBoolean())
					possibleEnemies.add(neighbor);
			}

			if (possibleEnemies.size() == 0)
				continue;

			Region enemy = possibleEnemies.get(RANDOM.nextInt(possibleEnemies.size()));

			if (RANDOM.nextInt(Math.abs(region.getDices() - enemy.getDices()) + 1) != 0 && region.getDices() != Game.DICE_MAX)
				continue;

			setRegionSelected(region);
			setRegionAttacked(enemy);

			if (startAttack())
				return;
		}

		endTurn();
	}

	private boolean startAttack() {
		if (_throwDice)
			return false;

		if (_regionAttacked == null || _regionSelected == null)
			return false;

		if (_regionAttacked.getPlayer() == null || _regionSelected.getPlayer() == null)
			return false;

		if (_regionSelected.getDices() == 1)
			return false;

		if (_regionAttacked.getPlayer() == _regionSelected.getPlayer())
			return false;

		clearDiceList();

		printLog(String.format(LanguageManager.getString("log.attack"), _regionSelected.getPlayerName(), _regionAttacked.getPlayerName())); //$NON-NLS-1$ //$NON-NLS-2$

		_throwDice = true;

		return true;
	}

	public void endUserTurn() {
		if (_throwDice || _activePlayer.isCPU())
			return;
		endTurn();
	}

	public void printLog(String text) {
		_frame.getJTextAreaLog().append(text + "\r\n"); //$NON-NLS-1$
		_frame.getJTextAreaLog().setCaretPosition(_frame.getJTextAreaLog().getText().trim().length());
	}

	public int getNumCPU() {
		return _numCPU;
	}

	public int getNumPlayer() {
		return _numPlayer;
	}

	public int getNumCountrys() {
		return _numCountries;
	}

	public void keyPressed(KeyEvent key) {
		if (_throwDice || _activePlayer.isCPU())
			return;

		switch (key.getKeyCode()) {
			// Player
			case 37:
				switchRegion(-1);
				break;
			case 39:
				switchRegion(1);
				break;

			// Neighbors
			case 38:
				switchAttack(-1);
				break;
			case 40:
				switchAttack(1);
				break;

			// Attack
			case 10:
			case 32:
				startAttack();
				break;

			// End Turn
			case 27:
				endUserTurn();
				break;
		}
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(Actions.EXIT))
			System.exit(0);
		else if (cmd.equals(Actions.INFO))
			showInfo();
		else if (cmd.equals(Actions.END_TURN))
			endUserTurn();
		else if (cmd.equals(Actions.NEW_GAME))
			newGame();
		else if (cmd.equals(Actions.TIMER)) {
			// Attack
			if (_throwDice)
				throwDice();
			else if (_activePlayer.isCPU())
				cpuAction();

			_frame.getGamePanel().repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (_throwDice)
			return;

		Region selected = null;
		for (Region region : _regionList) {
			if (region.getBorder().intersects(new Rectangle(e.getX(), e.getY(), 1, 1))) {
				if (selected != null)
					return;
				selected = region;
			}
		}

		// Deselect
		if (selected == _regionSelected) {
			setRegionSelected(null);
			setRegionAttacked(null);
		}

		// Select
		else if (_regionSelected != null) {
			if (selected.getPlayer() != _activePlayer && _regionSelected.isNeighbor(selected)) {
				setRegionAttacked(selected);
				startAttack();
			}
		}

		// No Selection
		else if (selected.getPlayer() == _activePlayer && selected.getDices() > 1) {
			setRegionSelected(selected);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}
}
