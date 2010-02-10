package de.core23.dicewars.helper;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.core23.dicewars.misc.Game;
import de.core23.dicewars.misc.Style;
import de.core23.dicewars.model.Region;

public class MapGenerator implements Style, Game {
	private static final Random RANDOM = new Random();

	private LinkedList<Region> _regions = new LinkedList<Region>();

	private Region[][] _map = new Region[NUM_BLOCK_X][NUM_BLOCK_Y];

	private boolean[][] _checkMap = new boolean[NUM_BLOCK_X][NUM_BLOCK_Y];

	private Region _regionWater = new Region();

	private Region getMap(int x, int y) {
		if (x < 0 || x >= NUM_BLOCK_X)
			return null;
		if (y < 0 || y >= NUM_BLOCK_Y)
			return null;
		return _map[x][y];
	}

	public LinkedList<Region> generate(int countries) {
		// Clear Regions
		_regions.clear();

		// Clear Map
		for (int x = 0; x < NUM_BLOCK_X; x++) {
			for (int y = 0; y < NUM_BLOCK_Y; y++) {
				_map[x][y] = _regionWater;
			}
		}

		// Create Regions
		for (int i = 0; i < NUM_BLOCK_X * NUM_BLOCK_Y * 4; i++) {
			int x = RANDOM.nextInt(NUM_BLOCK_X);
			int y = RANDOM.nextInt(NUM_BLOCK_Y);

			// Build
			if (getMap(x, y) == _regionWater) {
				if (_regions.size() < countries)
					createCity(x, y);
			}

			// Enlarge
			enlargeCity(x, y, true);
		}

		// Create Neighbors
		for (int x = 0; x < NUM_BLOCK_X; x++) {
			for (int y = 0; y < NUM_BLOCK_Y; y++) {
				for (int i = 0; i < 6; i++) {
					int odd = x % 2;

					Region region = getMap(x + HEXAGON_COORDS[i][odd][0], y + HEXAGON_COORDS[i][odd][1]);

					if (getMap(x, y) != region && region != _regionWater)
						getMap(x, y).addNeighbor(region);
				}
			}
		}

		// Remove Water
		for (Region region : _regions)
			region.removeNeighbor(_regionWater);

		// Create Borders
		for (int x = 0; x < NUM_BLOCK_X; x++) {
			for (int y = 0; y < NUM_BLOCK_Y; y++) {
				Region region = getMap(x, y);

				if (region == null)
					continue;
				if (region.getBorder().npoints > 0)
					continue;

				// Clear Checkmap
				for (int x2 = 0; x2 < NUM_BLOCK_X; x2++)
					for (int y2 = 0; y2 < NUM_BLOCK_Y; y2++)
						_checkMap[x2][y2] = false;

				region.setBorder(createBorder(x, y, _map[x][y], new Polygon(), 0));
			}
		}

		// Valid Map
		if (!isValidMap())
			generate(countries);

		return _regions;
	}

	private boolean isValidMap() {
		// No Neighbors
		for (Region region : _regions) {
			if (region.getNeighbors().size() == 0)
				return false;
		}

		// Different Continents
		List<Region> checkRegions = new ArrayList<Region>();
		for (Region rgn : _regions)
			checkRegions.add(rgn);
		filterRegions(_regions.getFirst(), checkRegions);
		if (checkRegions.size() > 0)
			return false;

		// Holes
		for (int x = 0; x < NUM_BLOCK_X; x++) {
			for (int y = 0; y < NUM_BLOCK_Y; y++) {
				if (isHole(x, y))
					return false;
			}
		}

		return true;
	}

	private void filterRegions(Region region, List<Region> checkRegions) {
		checkRegions.remove(region);
		for (Region rgn : region.getNeighbors()) {
			if (!checkRegions.contains(rgn))
				continue;
			filterRegions(rgn, checkRegions);
		}
	}

	private Polygon createBorder(int x, int y, Region region, Polygon poly, int start) {
		if (getMap(x, y) == null)
			return null;
		if (region != _map[x][y])
			return null;

		if (_checkMap[x][y])
			return poly;

		_checkMap[x][y] = true;

		// Calculation
		double pX = x;
		double pY = y;
		if (pX % 2 == 1)
			pY += 0.5;
		pX = MARGIN_LEFT + pX * 1.5 * BLOCK_SIZE;
		pY = MARGIN_TOP + pY * 2 * HEXAGON_HEIGHT;

		int[][] polyCords = { {(int) (pX + HEXAGON_WIDTH + BLOCK_SIZE), (int) (pY)},
			{(int) (pX + HEXAGON_WIDTH + BLOCK_SIZE + HEXAGON_WIDTH), (int) (pY + HEXAGON_HEIGHT)},
			{(int) (pX + HEXAGON_WIDTH + BLOCK_SIZE), (int) (pY + HEXAGON_HEIGHT + HEXAGON_HEIGHT)},
			{(int) (pX + HEXAGON_WIDTH), (int) (pY + HEXAGON_HEIGHT + HEXAGON_HEIGHT)}, {(int) (pX), (int) (pY + HEXAGON_HEIGHT)},
			{(int) (pX + HEXAGON_WIDTH), (int) (pY)}};

		for (int i = 0; i < 6; i++) {
			int odd = x % 2;
			int pos = (i + start) % 6;
			int next = (pos - 2 < 0) ? pos + 4 : pos - 2;

			if (createBorder(x + HEXAGON_COORDS[pos][odd][0], y + HEXAGON_COORDS[pos][odd][1], _map[x][y], poly, next) == null)
				poly.addPoint(polyCords[pos][0], polyCords[pos][1]);
		}
		return poly;
	}

	private boolean isHole(int x, int y) {
		if (getMap(x, y) != _regionWater)
			return false;
		return (getNeighbor(x, y, new boolean[NUM_BLOCK_X][NUM_BLOCK_Y]) != null);
	}

	private Region getNeighbor(int x, int y, boolean[][] checkMap) {
		checkMap[x][y] = true;

		Region regionNeighbor = null;

		// Get Region
		for (int i = 0; i < 5; i++) {
			int odd = x % 2;
			int newX = x + HEXAGON_COORDS[i][odd][0];
			int newY = y + HEXAGON_COORDS[i][odd][1];

			Region region = getMap(newX, newY);

			// Border
			if (region == null)
				return null;

			// Water
			if (region == _regionWater) {
				if (checkMap[newX][newY])
					continue;

				region = getNeighbor(newX, newY, checkMap);

				if (region == null)
					return null;
			}

			// Set Neighbor
			if (regionNeighbor == null)
				regionNeighbor = region;

			// Different Neighbor
			if (region != regionNeighbor)
				return null;
		}

		return regionNeighbor;
	}

	private void createCity(int x, int y) {
		// Calculate Free
		int free = 0;

		for (int i = 0; i < 6; i++) {
			int odd = x % 2;

			if (getMap(x + HEXAGON_COORDS[i][odd][0], y + HEXAGON_COORDS[i][odd][1]) == _regionWater)
				free++;
		}

		if (free < 5)
			return;

		// Add Start Dices
		_map[x][y] = new Region(x, y);
		_regions.add(_map[x][y]);

		// Enlarge
		enlargeCity(x, y, false);
	}

	private void enlargeCity(int x, int y, boolean isRandom) {
		if (getMap(x, y) == _regionWater)
			return;

		for (int i = 0; i < 6; i++) {
			int odd = x % 2;

			if (getMap(x + HEXAGON_COORDS[i][odd][0], y + HEXAGON_COORDS[i][odd][1]) == _regionWater && (RANDOM.nextBoolean() || !isRandom))
				_map[x + HEXAGON_COORDS[i][odd][0]][y + HEXAGON_COORDS[i][odd][1]] = _map[x][y];
		}
	}
}
