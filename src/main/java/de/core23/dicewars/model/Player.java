package de.core23.dicewars.model;

import java.awt.Color;
import java.util.LinkedList;

import de.core23.dicewars.misc.Game;
import de.core23.dicewars.helper.LanguageManager;

public class Player implements Game {
	private Color _color;

	private LinkedList<Region> _regions = new LinkedList<Region>();

	private int _number;

	private boolean _cpu = false;

	public Player(int num, boolean cpu) {
		this._number = num;
		this._color = new Color(COLORS[num]);
		this._cpu = cpu;
	}

	public Color getColor() {
		return _color;
	}

	public int getRegionSize() {
		return _regions.size();
	}

	public LinkedList<Region> getRegions() {
		return _regions;
	}

	public void addRegion(Region region) {
		if (region == null)
			return;
		_regions.add(region);
	}

	public void removeRegion(Region region) {
		_regions.remove(region);
	}

	public boolean isCPU() {
		return _cpu;
	}

	public int getNumber() {
		return _number;
	}

	public int getMaxRegionCluster() {
		int count = 0;
		for (Region region : _regions) {
			LinkedList<Region> clusterContainer = new LinkedList<Region>();
			count = Math.max(region.getRegionSize(clusterContainer).size(), count);
		}
		return count;
	}

	public String getName() {
		if (isCPU())
			return String.format(LanguageManager.getString("name.cpu.text"), (getNumber() + 1)); //$NON-NLS-1$
		else
			return String.format(LanguageManager.getString("name.player.text"), (getNumber() + 1)); //$NON-NLS-1$
	}

	public String toString() {
		return "Player [" + getName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
