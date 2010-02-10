package de.core23.dicewars.component;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JHyperlink extends JLabel implements MouseListener {
	private static final long serialVersionUID = 1L;

	private URL _url;

	public JHyperlink(String label, ImageIcon icon) {
		super();

		if (icon != null) {
			setIcon(icon);
			setSize(icon.getIconWidth(), icon.getIconHeight());
		}

		if (label != null && !label.isEmpty()) {
			setForeground(Color.BLUE);
			setText(label);
		}

		addMouseListener(this);
	}

	public JHyperlink(String label) {
		this(label, (ImageIcon) null);
	}

	public JHyperlink(ImageIcon icon, String link) {
		this(null, icon);
		setURL(link);
	}

	public JHyperlink(ImageIcon icon, URL url) {
		this(null, icon);
		setURL(url);
	}

	public JHyperlink(String label, String link) {
		this(label, (ImageIcon) null);
		setURL(link);
	}

	public JHyperlink(String label, URL url) {
		this(label, (ImageIcon) null);
		setURL(url);
	}

	public void setURL(String url) {
		try {
			this._url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void setURL(URL url) {
		this._url = url;
	}

	public URL getURL() {
		return _url;
	}

	public void mouseClicked(MouseEvent e) {
		JHyperlink self = (JHyperlink) e.getSource();
		if (self._url == null)
			return;
		try {
			Desktop.getDesktop().browse(new URI(self._url.toString()));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
		e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
