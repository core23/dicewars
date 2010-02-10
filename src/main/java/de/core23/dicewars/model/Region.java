package de.core23.dicewars.model;

import java.awt.Color;
import java.awt.Polygon;
import java.util.LinkedList;

import de.core23.dicewars.misc.Style;

public class Region {
	private Player _player = null;

	private int _dices = 1;

	private int _midX = 0;

	private int _midY = 0;

	private LinkedList<Region> _neighbors = new LinkedList<Region>();

	private Polygon _border = new Polygon();

	public Region() {
	}

	public Region(int midX, int midY) {
		double pX = midX;
		double pY = midY;
		if (pX % 2 == 1)
			pY += 0.5;
		pX = Style.MARGIN_LEFT + pX * 1.5 * Style.BLOCK_SIZE;
		pY = Style.MARGIN_TOP + pY * 2 * Style.HEXAGON_HEIGHT;
		this._midX = (int) pX;
		this._midY = (int) pY;
	}

	public Polygon getBorder() {
		return _border;
	}

	public void setBorder(Polygon border) {
		this._border = border;
	}

	public Player getPlayer() {
		return _player;
	}

	public Color getPlayerColor() {
		if (_player == null)
			return null;
		else
			return _player.getColor();
	}

	public String getPlayerName() {
		if (_player == null)
			return "";
		else
			return _player.getName();
	}

	public void setPlayer(Player newPlayer) {
		if (_player != null)
			_player.removeRegion(this);

		_player = newPlayer;

		if (newPlayer != null)
			_player.addRegion(this);
	}

	public int getDices() {
		return _dices;
	}

	public void setDices(int dices) {
		this._dices = dices;
	}

	public void addDice() {
		_dices++;
	}

	public int getMidX() {
		return _midX;
	}

	public int getMidY() {
		return _midY;
	}

	public void addNeighbor(Region region) {
		if (region == null)
			return;
		_neighbors.remove(region);
		_neighbors.add(region);
	}

	public void removeNeighbor(Region region) {
		_neighbors.remove(region);
	}

	public LinkedList<Region> getNeighbors() {
		return _neighbors;
	}

	public LinkedList<Region> getEnemyNeighbors() {
		LinkedList<Region> enemys = new LinkedList<Region>();
		for (Region neighbor : _neighbors) {
			if (neighbor.getPlayer() != getPlayer())
				enemys.add(neighbor);
		}
		return enemys;
	}

	public boolean isNeighbor(Region rgn) {
		return _neighbors.contains(rgn);
	}

	public LinkedList<Region> getRegionSize(LinkedList<Region> cluster) {
		cluster.add(this);

		for (Region neighbor : _neighbors) {
			if (neighbor.getPlayer() == this.getPlayer() && !cluster.contains(neighbor))
				neighbor.getRegionSize(cluster);
		}
		return cluster;
	}

	public String toString() {
		if (this == null)
			return "null"; //$NON-NLS-1$
		return "Region [" + _player + ", Dices: " + _dices + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
