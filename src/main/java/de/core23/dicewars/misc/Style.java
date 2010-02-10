package de.core23.dicewars.misc;

public interface Style {
	final int BLOCK_SIZE = 12;

	final int[][][] HEXAGON_COORDS = { { {1, -1}, {1, 0}}, { {1, 0}, {1, 1}}, { {0, 1}, {0, 1}}, { {-1, 0}, {-1, 1}}, { {-1, -1}, {-1, 0}}, { {0, -1}, {0, -1}}};

	final double HEXAGON_HEIGHT = (BLOCK_SIZE / 2 * Math.sqrt(3) * 0.9);

	final double HEXAGON_WIDTH = (BLOCK_SIZE / 2F);

	final int MARGIN_LEFT = 25;

	final int MARGIN_TOP = 25;

	final int MAP_WIDTH = (int) ((HEXAGON_WIDTH + BLOCK_SIZE) * Game.NUM_BLOCK_X);

	final int MAP_HEIGHT = (int) (HEXAGON_HEIGHT * 2 * (Game.NUM_BLOCK_Y + 1));
}
