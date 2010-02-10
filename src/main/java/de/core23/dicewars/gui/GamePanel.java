package de.core23.dicewars.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JPanel;

import de.core23.dicewars.misc.Game;
import de.core23.dicewars.misc.Style;
import de.core23.dicewars.model.Dice;
import de.core23.dicewars.model.Player;
import de.core23.dicewars.model.Region;

public class GamePanel extends JPanel implements Style {
	private static final long serialVersionUID = -8244546800871152854L;

	private Player _activePlayer;

	private LinkedList<Player> _playerList = new LinkedList<Player>();

	private Region _regionSelected = null;

	private Region _regionAttacked = null;

	private LinkedList<Dice> _diceList = new LinkedList<Dice>();

	private LinkedList<Region> _regionList = new LinkedList<Region>();

	public void setActivePlayer(Player activePlayer) {
		this._activePlayer = activePlayer;
	}

	public void setRegionSelected(Region regionSelected) {
		this._regionSelected = regionSelected;
	}

	public void setRegionAttacked(Region regionAttacked) {
		this._regionAttacked = regionAttacked;
	}

	public void setRegionList(LinkedList<Region> regionList) {
		_regionList = regionList;
	}

	public void setPlayerList(LinkedList<Player> playerList) {
		_playerList = playerList;
	}

	public void clearDiceList() {
		_diceList.clear();
	}

	public void addDice(Dice dice) {
		_diceList.add(dice);
	}

	public Polygon getDicePolygon() {
		Polygon poly = new Polygon();
		poly.addPoint(8, 8);
		poly.addPoint(0, 8);
		poly.addPoint(0, 16);
		poly.addPoint(8, 16);
		poly.addPoint(12, 12);
		poly.addPoint(12, 4);
		poly.addPoint(4, 4);
		poly.addPoint(0, 8);
		poly.addPoint(8, 8);
		poly.addPoint(8, 16);
		poly.addPoint(8, 8);
		poly.addPoint(12, 4);
		return poly;
	}

	public void paint(Graphics g) {
		// Background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draw Regions
		for (Region region : _regionList) {
			if (region == _regionSelected)
				g.setColor(region.getPlayerColor().darker());
			else if (region == _regionAttacked)
				g.setColor(region.getPlayerColor().brighter());
			else
				g.setColor(region.getPlayerColor());
			g.fillPolygon(region.getBorder());
		}

		// Draw Borders
		g.setColor(Color.BLACK);
		for (Region region : _regionList)
			g.drawPolygon(region.getBorder());

		// Draw Stacks
		Polygon poly = getDicePolygon();
		g.setColor(Color.BLACK);
		for (Region city : _regionList) {
			for (int i = 0; i < city.getDices(); i++) {
				int pX = (i > (Game.DICE_MAX / 2) - 1 ? 16 : 4);
				int pY = (i % (Game.DICE_MAX / 2)) * 8;

				poly.translate(city.getMidX() + pX, city.getMidY() - pY);

				g.setColor(city.getPlayerColor().brighter().brighter());
				g.fillPolygon(poly);

				g.setColor(Color.BLACK);
				g.drawPolygon(poly);

				poly.translate(-city.getMidX() - pX, -city.getMidY() + pY);
			}
		}

		// Draw Players
		g.setFont(new Font("Helvetica", Font.BOLD, 12)); //$NON-NLS-1$
		FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
		if (_playerList.size() > 0) {
			for (int i = 0; i < _playerList.size(); i++) {
				Player player = _playerList.get(i);

				// Highlight Active
				if (player == _activePlayer) {
					g.setColor(Color.WHITE);
					g.fillRect((int) (MARGIN_LEFT + MAP_WIDTH / _playerList.size() * (i)), (int) (MARGIN_TOP + MAP_HEIGHT) + 1, (int) 90 + 6, 20);
					g.setColor(player.getColor());
					g.drawRect((int) (MARGIN_LEFT + MAP_WIDTH / _playerList.size() * (i)), (int) (MARGIN_TOP + MAP_HEIGHT) + 1, (int) 90 + 6, 20);
				}

				g.setColor(player.getColor());

				String text = player.getName() + ":"; //$NON-NLS-1$
				Rectangle2D rectFont = g.getFont().getStringBounds(text, frc);
				g.drawString(text, (int) (MARGIN_LEFT + MAP_WIDTH / _playerList.size() * (i) + 4), (int) (MARGIN_TOP + MAP_HEIGHT + rectFont.getHeight()));

				text = player.getMaxRegionCluster() + ""; //$NON-NLS-1$
				rectFont = g.getFont().getStringBounds(text, frc);
				g.drawString(text, (int) (MARGIN_LEFT + MAP_WIDTH / _playerList.size() * (i) + 4 + 90 - rectFont.getWidth()),
					(int) (MARGIN_TOP + MAP_HEIGHT + rectFont.getHeight()));

			}
		}

		// Draw Dices
		if (_diceList.size() > 0) {
			g.setFont(new Font("Helvetica", Font.BOLD, 12)); //$NON-NLS-1$
			frc = g.getFontMetrics().getFontRenderContext();

			for (int i = 0; i < _diceList.size(); i++) {
				g.setColor(Color.WHITE);
				g.fillRect(MARGIN_LEFT + 30 * i, MARGIN_TOP + MAP_HEIGHT + 30, 20, 20);

				g.setColor(_diceList.get(i).getColor());
				g.drawRect(MARGIN_LEFT + 30 * i, MARGIN_TOP + MAP_HEIGHT + 30, 20, 20);

				int value = _diceList.get(i).getValue();

				// Middle
				if (value == 1 || value == 3 || value == 5) {
					g.fillOval(MARGIN_LEFT + 30 * i + 8, MARGIN_TOP + MAP_HEIGHT + 30 + 8, 4, 4);
				}
				// TL / BR
				if (value > 1) {
					g.fillOval(MARGIN_LEFT + 30 * i + 2, MARGIN_TOP + MAP_HEIGHT + 30 + 2, 4, 4);
					g.fillOval(MARGIN_LEFT + 30 * i + 14, MARGIN_TOP + MAP_HEIGHT + 30 + 14, 4, 4);

				}
				// TR / BL
				if (value > 3) {
					g.fillOval(MARGIN_LEFT + 30 * i + 14, MARGIN_TOP + MAP_HEIGHT + 30 + 2, 4, 4);
					g.fillOval(MARGIN_LEFT + 30 * i + 2, MARGIN_TOP + MAP_HEIGHT + 30 + 14, 4, 4);
				}

				// Six
				if (value == 6) {
					g.fillOval(MARGIN_LEFT + 30 * i + 2, MARGIN_TOP + MAP_HEIGHT + 30 + 8, 4, 4);
					g.fillOval(MARGIN_LEFT + 30 * i + 14, MARGIN_TOP + MAP_HEIGHT + 30 + 8, 4, 4);
				}
			}
		}
	}
}
