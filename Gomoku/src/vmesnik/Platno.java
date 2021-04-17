package vmesnik;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JPanel;

import logika.Igra;

@SuppressWarnings("serial")
public class Platno extends JPanel {
	
	protected Color barva_igralca_1 = Color.BLACK;
	protected Color barva_igralca_2 = Color.WHITE;
	protected Color barva_ozadja = Color.GRAY;
	protected Igra igra;
	protected GridLayout mreza;

	public Platno(int sirina, int visina, Igra igra) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		setBackground(barva_ozadja);
		this.igra = igra;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.WHITE);
		
		int polje_x = igra.dimenzija_polja_x();
		int polje_y = igra.dimenzija_polja_y();

		mreza = new GridLayout(polje_x, polje_y);
		
        for (int i = 0; i < polje_x * polje_y; i++) {
        	this.add(new JButton("A"));	
        }
		
	}
}