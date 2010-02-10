package de.core23.dicewars.model;

import java.awt.Color;
import java.util.Random;

public class Dice {
	private static Random RANDOM = new Random();

	private int _value;

	private Region _region;

	private Color _color;

	public Dice(Region region) {
		this._region = region;
		this._value = RANDOM.nextInt(6) + 1;
		this._color = region.getPlayerColor();
	}

	public Region getRegion() {
		return _region;
	}

	public int getValue() {
		return _value;
	}

	public Color getColor() {
		return _color;
	}
}
