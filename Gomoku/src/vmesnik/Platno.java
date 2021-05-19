package vmesnik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import logika.Igra;

import splosno.Koordinati;
import vodja.Vodja;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	protected Color barva_igralca_1 = Color.BLACK;
	protected Color barva_igralca_2 = Color.RED;
	protected Color barva_ozadja = Color.GRAY;
	public Igra igra;
	protected HashMap<Integer, int[]> kvadratki;
	protected HashMap<Integer, String> zemljevid_kvadratkov;
	protected int stranica_kvadratka;
	protected boolean prvi = true;
	protected HashMap<String, HashSet<Integer>> izbrana_polja;
	protected Okno okno;

	public Platno(int sirina, int visina, Okno okno, Igra igra) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		this.okno = okno;
		this.igra = igra;
	    this.izbrana_polja = new HashMap<String, HashSet<Integer>>();
	    this.izbrana_polja.put("1", new HashSet<Integer>());
	    this.izbrana_polja.put("2", new HashSet<Integer>());
	    this.kvadratki = new HashMap<Integer, int[]>();
	    this.zemljevid_kvadratkov = new HashMap<Integer, String>();
	    this.kartiranje_kvadratkov();
	    
	    addMouseListener(this); //s tem povemo da se bo objekt odzival na misko
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true); //platno sposobno prejet fokus in bo reagiralo na dogodke s tipkovnico
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		this.kvadratki = new HashMap<Integer, int[]>();
		super.paintComponent(g);
		
		if (prvi) {			
			g.setColor(barva_igralca_1);
		}
		else g.setColor(barva_igralca_2);
		
		Rectangle r = this.getBounds();
		int v_polja = r.height;
		int s_polja = r.width;
		
		
		int dim_x = igra.dimenzija_polja_x() + 1;
		int dim_y = igra.dimenzija_polja_y() + 1;

		stranica_kvadratka = Math.min(v_polja / dim_y, s_polja / dim_x);
		int sirina_igralnega_polja = stranica_kvadratka * dim_x;
		int visina_igralnega_polja = stranica_kvadratka * dim_y;
		int prazno_obmocje_x = s_polja - sirina_igralnega_polja;
		int prazno_obmocje_y = v_polja - visina_igralnega_polja;
		
		
		for (int i = 0; i < dim_y; i++) {
			for (int j = 0; j < dim_x; j++) {
				if (i == 0) {
					if (j == 0) { // Treba spremeniti design ikonce
						g.fillOval(prazno_obmocje_x / 2 + stranica_kvadratka / 2,
								   prazno_obmocje_y / 2 + stranica_kvadratka / 2,
								   stranica_kvadratka / 2,
								   stranica_kvadratka / 2);
						g.setColor(Color.BLACK);
						continue;
					}
					String vrednost = "" + j;
					g.drawString(vrednost, (int) Math.round((j + 0.4) * stranica_kvadratka + prazno_obmocje_x / 2)
							, (int) Math.round((i  + 0.8) * stranica_kvadratka + prazno_obmocje_y / 2));
				}
				else{
					if (j == 0) {
						char crka = (char) (('A' + i) - 1);
						String vrednost = Character.toString(crka);
						g.drawString(vrednost, (int) Math.round((j + 0.65) * stranica_kvadratka + prazno_obmocje_x / 2)
								, (int) Math.round((i  + 0.6) * stranica_kvadratka + prazno_obmocje_y / 2));
					}
					else {
						g.drawRect(j * stranica_kvadratka + prazno_obmocje_x / 2,
							       i * stranica_kvadratka + prazno_obmocje_y / 2,
							       stranica_kvadratka, stranica_kvadratka);
						int[] nova_tabela = new int[2];
						nova_tabela[0] = (int) Math.round((j + 0.5) * stranica_kvadratka + prazno_obmocje_x / 2);
						nova_tabela[1] = (int) Math.round((i + 0.5) * stranica_kvadratka + prazno_obmocje_y / 2);
						this.kvadratki.put(i * dim_x + j, nova_tabela);
					}
				}
			}
		}
		
		
		for (String  igralec : this.izbrana_polja.keySet()) {
			if (this.izbrana_polja.get(igralec) != null) {
				for (int izbranec : this.izbrana_polja.get(igralec)) { 
					int[] tabela_koordinat = kvadratki.get(izbranec);
					int x = tabela_koordinat[0];
					int y = tabela_koordinat[1];
					
					if (igralec == "1") g.setColor(barva_igralca_1);
					else g.setColor(barva_igralca_2);
					
					g.fillOval(x - (stranica_kvadratka / 4), y - (stranica_kvadratka / 4),
							  (int) Math.round(stranica_kvadratka / 2),
							  (int) Math.round(stranica_kvadratka / 2));
				}
			}
		}
	}
	
	public void kartiranje_kvadratkov() {
		int dim_x = igra.dimenzija_polja_x() + 1;
		int dim_y = igra.dimenzija_polja_y() + 1;
		char trenutna_crka = '\u0000';
		
		for (int i = 0; i < dim_y; i++) {
			for (int j = 0; j < dim_x; j++) {
				if (j == 0) {
					char crka = (char) (('A' + i) - 1);
					trenutna_crka = crka;
				}
				else {
					if (i != 0) {
						this.zemljevid_kvadratkov.put(i * dim_x + j, "" + trenutna_crka + (j - 1));
					}
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int dim_x = igra.dimenzija_polja_x();
		for (Map.Entry<Integer, int[]> kvadratek : kvadratki.entrySet()) {
			int kljuc = kvadratek.getKey();
			if (kljuc <= dim_x || (kljuc % (dim_x + 1) == 0)) {
				System.out.println("" + kljuc);
				continue;
			}
			if (this.izbrana_polja.get("1").contains(kljuc) || this.izbrana_polja.get("2").contains(kljuc)) continue;
			if (Math.abs(kvadratek.getValue()[0] - x) < (stranica_kvadratka / 2) &&
					Math.abs(kvadratek.getValue()[1] - y) < (stranica_kvadratka / 2)) {
				if (prvi) this.izbrana_polja.get("1").add(kvadratek.getKey());
				else this.izbrana_polja.get("2").add(kvadratek.getKey());
				this.prvi = !prvi;
				Vodja.igrajClovekovoPotezo(this.pretvori_iz_kvadrata(kljuc));
				break;
			}
		}
		repaint();
		if (igra.je_konec_igre()) {
			okno.konec_igre(true);
			this.nova_igra();
		}
		
		if (igra.mnozica_potez().size() == 0) {
			okno.konec_igre(false);
			this.nova_igra();
		}
	}
	
	private Koordinati pretvori_iz_kvadrata(int n) {
		String zemljevidne_koordinate = this.zemljevid_kvadratkov.get(n);
		char crka = zemljevidne_koordinate.charAt(0);
		String stevilo = zemljevidne_koordinate.substring(1);
		return new Koordinati(crka - 'A', Integer.valueOf(stevilo));
	}
	
	public void nova_igra() { // Treba je nastaviti, da ohrani uporabnikove nastavitve
		igra = new Igra();
		prvi = true;
		izbrana_polja = new HashMap<String, HashSet<Integer>>();
	    this.izbrana_polja.put("1", new HashSet<Integer>());
	    this.izbrana_polja.put("2", new HashSet<Integer>());
	    this.kartiranje_kvadratkov();
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

// {"1" : {{1, 5}, {8, 4}}; "2" : {{5, 2}}}