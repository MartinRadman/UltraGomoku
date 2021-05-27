package vmesnik;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logika.Igra;

import splosno.Koordinati;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	protected Color barva_igralca_1 = Color.BLACK;
	protected Color barva_igralca_2 = Color.RED;
	protected Color barva_ovala_1 = Color.DARK_GRAY;
	protected Color barva_ovala_2 = Color.DARK_GRAY;
	protected Color barva_ovala_3 = Color.DARK_GRAY;
	protected Color barva_ozadja = Color.GRAY;
	protected Color barva_pisave = Color.YELLOW;
	protected Color aktivna_barva = Color.RED;
	protected Color zmagovalna_barva = new Color(255,215,0);
	
	public Igra igra;
	protected HashMap<Integer, int[]> kvadratki;
	protected HashMap<Integer, String> zemljevid_kvadratkov;
	protected int stranica_kvadratka;
	protected boolean prvi = true;
	protected HashMap<String, HashSet<Integer>> izbrana_polja;
	protected Vodja vodja;
	protected Okno okno;
	protected boolean osnovni_meni = true;
	
	protected int start;
	protected int dolzina_naslova;
	protected int v_polja;
	protected int s_polja;
	protected int velikost_pisave;

	public Platno(int sirina, int visina, Vodja vodja, Okno okno, Igra igra) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		this.vodja = vodja;
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
		
		Graphics2D g2 = (Graphics2D)g;
		
		Rectangle r = this.getBounds();
		v_polja = r.height;
		s_polja = r.width;
		
		if (osnovni_meni) {
			String naslov = "GOMOKU";
			
			velikost_pisave = (int) Math.min(0.1 * s_polja, 0.1 * v_polja);
			g.setFont(new Font("Times New Roman", Font.PLAIN, velikost_pisave));
			
			dolzina_naslova = (int) g.getFontMetrics().getStringBounds(naslov, g).getWidth();
			start = (int) Math.round(s_polja / 2) - dolzina_naslova / 2;

			g.drawString(naslov, start, (int) Math.round(0.2 * v_polja));
			
			g.setColor(barva_ovala_1);
			g.fillOval(start, (int) Math.round(0.4 * v_polja) - velikost_pisave / 2, dolzina_naslova, velikost_pisave / 2);
			
			g.setColor(barva_ovala_2);
			g.fillOval(start, (int) Math.round(0.4 * v_polja) + velikost_pisave / 2, dolzina_naslova, velikost_pisave / 2);
			
			g.setColor(barva_ovala_3);
			g.fillOval(start, (int) Math.round(0.4 * v_polja) + 3 * velikost_pisave / 2, dolzina_naslova, velikost_pisave / 2);
			
			
			g.setColor(barva_pisave);
			
			String gumb1 = "NOVA IGRA";
			
			int velikost_pisave1 = (int) velikost_pisave / 3;
			g.setFont(new Font("Times New Roman", Font.PLAIN, velikost_pisave1));
			
			int dolzina_naslova1 = (int) g.getFontMetrics().getStringBounds(gumb1, g).getWidth();
			int start1 = (int) Math.round(s_polja / 2) - dolzina_naslova1 / 2;

			g.drawString(gumb1, start1, (int) Math.round(0.4 * v_polja) - velikost_pisave1 / 2);
			
			
			String gumb2 = "NOVA IGRA PROTI RAČUNALNIKU";
			
			int velikost_pisave2 = velikost_pisave / 5;
			g.setFont(new Font("Times New Roman", Font.PLAIN, velikost_pisave2));
			
			int dolzina_naslova2 = (int) g.getFontMetrics().getStringBounds(gumb2, g).getWidth();
			int start2 = (int) Math.round(s_polja / 2) - dolzina_naslova2 / 2;

			g.drawString(gumb2, start2, (int) Math.round(0.4 * v_polja) + velikost_pisave - velikost_pisave2);
			
			
			String gumb3 = "NOVA IGRA PO MERI";
			
			int velikost_pisave3 =velikost_pisave / 4;
			g.setFont(new Font("Times New Roman", Font.PLAIN, velikost_pisave3));
			
			int dolzina_naslova3 = (int) g.getFontMetrics().getStringBounds(gumb3, g).getWidth();
			int start3 = (int) Math.round(s_polja / 2) - dolzina_naslova3 / 2;

			g.drawString(gumb3, start3, (int) Math.round(0.4 * v_polja) + 2 * velikost_pisave - velikost_pisave3 / 2);
			
			
		}
		
		else {
			if (prvi) {			
				g.setColor(barva_igralca_1);
			}
			else g.setColor(barva_igralca_2);
			
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
		
		
		if (igra.je_konec_igre()) {
			g2.setColor(zmagovalna_barva);
			g2.setStroke(new BasicStroke((int) (0.1 * stranica_kvadratka)));
			List<Koordinati> zmagovalna_poteza = igra.zmagovalna_poteza();
			
			Koordinati kvadratek1 = zmagovalna_poteza.get(0);
			Koordinati kvadratek2 = zmagovalna_poteza.get(zmagovalna_poteza.size() - 1);
			
			int st_kvadratka1 = (kvadratek1.getY() + 1) * (igra.dimenzija_polja_x() + 1) + kvadratek1.getX() + 1;
			int st_kvadratka2 = (kvadratek2.getY() + 1) * (igra.dimenzija_polja_x() + 1) + kvadratek2.getX() + 1;
			
			int[] sredisce1 = kvadratki.get(st_kvadratka1);
			int[] sredisce2 = kvadratki.get(st_kvadratka2);
			
			g.drawLine(sredisce1[0], sredisce1[1], sredisce2[0], sredisce2[1]);
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
		if (osnovni_meni) {
			int x1 = start + dolzina_naslova / 2;
			int y1 = (int) Math.round(0.4 * v_polja) - velikost_pisave / 2 + velikost_pisave / 4; // prepreči večkratni izračun koordinat
			
			int x2 = x1;
			int y2 = (int) Math.round(0.4 * v_polja) + velikost_pisave / 2 + velikost_pisave / 4;
			
			int x3 = x1;
			int y3 = (int) Math.round(0.4 * v_polja) + 3 * velikost_pisave / 2 + velikost_pisave / 4;
			
			if (Math.abs(x - x1) <= dolzina_naslova / 2 && Math.abs(y - y1) <= velikost_pisave / 2) {
				Okno okno = new Okno();
				okno.pack();
				okno.setVisible(true);
				okno.platno.osnovni_meni = false;
				okno.osvezi_vmesnik();
				okno.osvezi_vmesnik();
			}
			
			if (Math.abs(x - x2) <= dolzina_naslova / 2 && Math.abs(y - y2) <= velikost_pisave / 2) {
				Okno okno = new Okno(15, 15, "1. igralec", "2. igralec", VrstaIgralca.C, VrstaIgralca.R);
				okno.pack();
				okno.setVisible(true);
				okno.platno.osnovni_meni = false;
				okno.osvezi_vmesnik();
			}
			
			if (Math.abs(x - x3) <= dolzina_naslova / 2 && Math.abs(y - y3) <= velikost_pisave / 2) { // odpravi balastno kodo
				JFrame okence = new JFrame ("Igra po meri");
		        Okence podatki = new Okence();
		        okence.getContentPane().add(podatki);
		        okence.pack();
		        okence.setVisible (true);
		        
		        podatki.okay.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	 Okno okno;
		            	 // debug vrsta igralca
		            	 	int sirina_igralnega_polja = podatki.x.getValue();
		    			    if (sirina_igralnega_polja == 0) sirina_igralnega_polja = 15;
		    			    
		    			    int visina_igralnega_polja = podatki.y.getValue();
		    			    if (visina_igralnega_polja == 0) visina_igralnega_polja = 15;
		    			    
		    			    String igralec1_ime = podatki.player_1.getText();
		    			    if (igralec1_ime.isBlank()) igralec1_ime = "1. igralec";
		    			    
		    			    String igralec2_ime = podatki.player_2.getText();
		    			    if (igralec2_ime.isBlank()) igralec2_ime = "2. igralec";
		    			    if (igralec2_ime.equals(igralec1_ime)) {
		    			    	igralec1_ime = igralec1_ime + " (1)";
		    			    	igralec2_ime = igralec2_ime + " (2)";
		    			    }
		    			    
		    			    boolean je_racunalnik_1 = podatki.comp_1.isSelected();
		    			    boolean je_racunalnik_2 = podatki.comp_2.isSelected();
		    			    
		    			    if (je_racunalnik_1) {
		    			    	okno = (je_racunalnik_2 == true) 
		    			    	       ? new Okno(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime, VrstaIgralca.R, VrstaIgralca.R)
		    			    	       : new Okno(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime, VrstaIgralca.R, VrstaIgralca.C);
		    			    }
		    			    else {
		    			    	okno = (je_racunalnik_2 == true)
		    			    		   ? new Okno(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime, VrstaIgralca.C, VrstaIgralca.R)
		    			    		   : new Okno(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime, VrstaIgralca.C, VrstaIgralca.C);
		    			    }
		    			    okence.dispose();
		    			    okno.platno.osnovni_meni = false;
		    			    okno.osvezi_vmesnik();
		    			    okno.pack();
		    				okno.setVisible(true);
		            }
		        });
		        
		        podatki.back.addActionListener(new ActionListener() {

		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	okence.dispose();
		            }
		        });
			    
			}
		}
		else if (vodja.clovekNaVrsti()) {
			int dim_x = igra.dimenzija_polja_x();
			for (Map.Entry<Integer, int[]> kvadratek : kvadratki.entrySet()) {
				int kljuc = kvadratek.getKey();
				if (kljuc <= dim_x || (kljuc % (dim_x + 1) == 0)) {
					continue;
				}
				if (this.izbrana_polja.get("1").contains(kljuc) || this.izbrana_polja.get("2").contains(kljuc)) continue;
				if (Math.abs(kvadratek.getValue()[0] - x) < (stranica_kvadratka / 2) &&
						Math.abs(kvadratek.getValue()[1] - y) < (stranica_kvadratka / 2)) {
					if (prvi) this.izbrana_polja.get("1").add(kvadratek.getKey());
					else this.izbrana_polja.get("2").add(kvadratek.getKey());
					this.prvi = !prvi;
					vodja.igrajClovekovoPotezo(this.pretvori_iz_kvadrata(kljuc));
					break;
				}
			}
			repaint();
		}
	}
	
	private Koordinati pretvori_iz_kvadrata(int n) {
		String zemljevidne_koordinate = this.zemljevid_kvadratkov.get(n);
		char crka = zemljevidne_koordinate.charAt(0);
		String stevilo = zemljevidne_koordinate.substring(1);
		return new Koordinati(Integer.valueOf(stevilo), crka - 'A');
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
		int x = e.getX();
		int y = e.getY();
		
		int x1 = start + dolzina_naslova / 2;
		int y1 = (int) Math.round(0.4 * v_polja) - velikost_pisave / 2 + velikost_pisave / 4; // prepreči večkratni izračun koordinat
		
		int x2 = x1;
		int y2 = (int) Math.round(0.4 * v_polja) + velikost_pisave / 2 + velikost_pisave / 4;
		
		int x3 = x1;
		int y3 = (int) Math.round(0.4 * v_polja) + 3 * velikost_pisave / 2 + velikost_pisave / 4;
		
		if (Math.abs(x - x1) <= dolzina_naslova / 2 && Math.abs(y - y1) <= velikost_pisave / 2) {
			barva_ovala_1 = Color.red;
		}
		
		if (Math.abs(x - x2) <= dolzina_naslova / 2 && Math.abs(y - y2) <= velikost_pisave / 2) {
			barva_ovala_2 = Color.red;
		}
		
		if (Math.abs(x - x3) <= dolzina_naslova / 2 && Math.abs(y - y3) <= velikost_pisave / 2) { // odpravi balastno kodo
			barva_ovala_3 = Color.red;		
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		barva_ovala_1 = barva_ovala_2 = barva_ovala_3 = Color.DARK_GRAY;
		repaint();
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