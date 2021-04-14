package vmesnik;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	protected Graf graf;
	protected Tocka aktivnaTocka;
	protected Set<Tocka> izbraneTocke;
		
	protected Color barvaPovezave;
	protected Color barvaTocke;
	protected Color barvaAktivneTocke;
	protected Color barvaIzbraneTocke;
	protected Color barvaRoba;
	protected float debelinaPovezave;
	protected float debelinaRoba;
	protected int polmer;
	
	public Platno(int sirina, int visina) {
		super(); //poklicemo konstruktor nadrazreda (v tem primeru JPanel)
		setPreferredSize(new Dimension(sirina, visina)); //s tem smo nastavili zeljeno veliokst
		graf = null;
		aktivnaTocka = null;
		izbraneTocke = new HashSet<Tocka>();
		barvaPovezave = Color.BLACK;
		barvaTocke = Color.RED;
		barvaAktivneTocke = Color.MAGENTA;
		barvaIzbraneTocke = Color.YELLOW;
		barvaRoba = Color.BLUE;
		debelinaPovezave = 2;
		debelinaRoba = 10;
		polmer = 10;
		
		addMouseListener(this); //s tem povemo da se bo objekt odzival na misko
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true); //platno sposobno prejet fokus in bo reagiralo na dogodke s tipkovnico
	}
	
	public void nastaviGraf(Graf graf) {  //public void nastaviGraf(Graf g)
		this.graf = graf;					// graf = g;
		aktivnaTocka = null;
		izbraneTocke.clear();                 // pobrise mnozico oz. jo sprazni
		repaint();                         //metoda, ki obnavlja sliko
	}
	
	private static int round(double x) {
		return (int)(x + 0.5);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);  //to moramo poklicat, da se bo slika pravilno osvezevala
		Graphics2D g2 = (Graphics2D)g;
	
		if (graf == null) return;

		
		//narisemo poveave
		g2.setColor(barvaPovezave);
		g2.setStroke(new BasicStroke(debelinaPovezave));
		for (Tocka v: graf.tocke.values()) {
			for (Tocka u: v.sosedi) {
				if (v.ime.compareTo(u.ime) < 0){
				g.drawLine(round(v.x), round(v.y), round(u.x), round(u.y));
				}
			}
		}
		
		//narisemo tocke
		int premer = 2 * polmer + 1;
		g2.setStroke(new BasicStroke(premer));
		for (Tocka v : graf.tocke.values()) {
			if (v == aktivnaTocka) g.setColor(barvaAktivneTocke);
			else if (izbraneTocke.contains(v)) g.setColor(barvaIzbraneTocke);
			else // g.setColor(barvaRoba);
			// g.drawOval(round(v.x) - polmer - round(debelinaRoba)/2, round(v.y) - polmer - round(debelinaRoba)/2, premer + round(debelinaRoba)/2, premer + round(debelinaRoba)/2);
			g.setColor(barvaTocke);
			g.fillOval(round(v.x) - polmer, round(v.y) - polmer, premer, premer);
		}		
		
		
		
	}

	private int stariX, stariY;
	private int klikX, klikY;
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		klikX = x;
		klikY = y;
		stariX = x;
		stariY = y;
		Tocka izbranaTocka = null;
		for (Tocka u : graf.tocke.values()) {
			if( (u.x - x) * (u.x - x) + (u.y - y) * (u.y - y) < polmer * polmer)  aktivnaTocka = u;
		}
		
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		// se ne premakne
		if ( x == klikX && y == klikY) {
			if (aktivnaTocka != null) {
				if (izbraneTocke.contains(aktivnaTocka)) izbraneTocke.remove(aktivnaTocka);
				else izbraneTocke.add(aktivnaTocka);
				}
			else {
				Tocka v = graf.dodajTocko();
				v.x = x; 
				v.y = y;
				for (Tocka u : izbraneTocke) {
					graf.dodajPovezavo(v, u);
				}
			}
		}
		aktivnaTocka = null;
		repaint();
		
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (aktivnaTocka != null) {
			int razlikaX = x - stariX;
			int razlikaY = y - stariY;
			aktivnaTocka.x += razlikaX;
			aktivnaTocka.y += razlikaY;
		}
		else { 
			for (Tocka u: izbraneTocke) {
				int razlikaX = x - stariX;
				int razlikaY = y - stariY;
				u.x += razlikaX;
				u.y += razlikaY;
			}
		}
		stariX = x;
		stariY = y;
		repaint();
	
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		char tipka = e.getKeyChar();
		if (tipka == 'a') {
			izbraneTocke.addAll(graf.tocke.values());
		}
		else if (tipka == 's') {
			izbraneTocke.clear();
		}
		else if (tipka == 't') {
			for (Tocka v : izbraneTocke) graf.odstraniTocko(v);
					izbraneTocke.clear();
		}
		else if (tipka == 'p') {
			for (Tocka v : izbraneTocke) {
				for (Tocka u :izbraneTocke) {graf.odstraniPovezavo(v, u);}
			}
		}
		
		repaint();
	}
	// teh ne bomo potrebovali
	
	@Override
	public void mouseClicked(MouseEvent e) {	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {}


	@Override
	public void mouseMoved(MouseEvent e) {}


	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F5) {
			for (Tocka v : graf.tocke.values()) izbraneTocke.add(v);
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	
}
