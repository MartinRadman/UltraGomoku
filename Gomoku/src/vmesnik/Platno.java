package vmesnik;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Platno extends JPanel {

	public Platno(int sirina, int visina) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
	}
}
