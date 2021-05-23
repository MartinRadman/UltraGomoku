/* Potrebni popravki:
   -undo move
   -ozadje
   -keyboard shortcuts
   -ohranitev nastavitev
   -spremljanje časa - šahovska ura?
   -polepšava okenca za novo igro po meri
*/

package vmesnik;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logika.Igra;
import logika.Igra.Igralec;
import splosno.KdoIgra;
import splosno.Koordinati;
import vodja.Vodja;
import vodja.VrstaIgralca;


@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	protected Vodja vodja;
	protected Platno platno;
	
	private Igra igra;
	
	protected String ime_igralca_1;
	protected String ime_igralca_2;
	
	private JMenuItem menuBarvaIgralca1;
	private JMenuItem menuBarvaIgralca2;
	
	private JMenuItem menuOsnovnaIgra;
	private JMenuItem menuIgraProtiRacunalniku;
	private JMenuItem menuIgraPoMeri;
	
	public int rezultat;
	
	public Okno(int sirina_igralnega_polja, int visina_igralnega_polja, String igralec1_ime, String igralec2_ime, VrstaIgralca igralec1, VrstaIgralca igralec2) {
		super();
		setTitle("Gomoku");
		
		Map<Igralec, VrstaIgralca> vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
		vrstaIgralca.put(Igralec.O, igralec1);
		vrstaIgralca.put(Igralec.X, igralec2);
		
		Map<Igralec, KdoIgra> kdoIgra = new EnumMap<Igralec, KdoIgra>(Igralec.class);
		kdoIgra.put(Igralec.O, new KdoIgra(igralec1_ime));
		kdoIgra.put(Igralec.X, new KdoIgra(igralec2_ime));
		
		Vodja vodja = new Vodja(this, vrstaIgralca, kdoIgra);
		
		vodja.igramoNovoIgro(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime);
		
		igra = vodja.igra();
		
		platno = new Platno(800, 800, vodja, this, igra);
		add(platno);
		
		String[] imena_igralcev = igra.imena_igralcev();
		ime_igralca_1 = imena_igralcev[0];
		ime_igralca_2 = imena_igralcev[1];
		
		vodja.igramo();
		
		//menuji
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuNovaIgra = dodajMenuNaMenu(menuIgra, "Nova igra");
		
		menuOsnovnaIgra = dodajMenuItem(menuNovaIgra, "Nova osnovna igra");
		menuIgraProtiRacunalniku = dodajMenuItem(menuNovaIgra, "Nova igra proti računalniku");
		menuIgraPoMeri = dodajMenuItem(menuNovaIgra, "Nova igra po meri ...");
		
		JMenu menuPrilagoditve = dodajMenu(menubar, "Prilagoditve");
		JMenu menuBarvaIgralcev = dodajMenuNaMenu(menuPrilagoditve, "Spremeni barvo igralcev ...");
		
		menuBarvaIgralca1 = dodajMenuItem(menuBarvaIgralcev, "Barva igralca " + ime_igralca_1 + " ..."); // Treba je popraviti izpis imenov igralcev
		menuBarvaIgralca2 = dodajMenuItem(menuBarvaIgralcev, "Barva igralca " + ime_igralca_2 + " ...");
	
		
	}
	
	public Okno() {
		this(15, 15, "1. igralec", "2. igralec", VrstaIgralca.C, VrstaIgralca.C);
	}
	 
	
	public JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;	
	}
	
	public JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}
	
	public JMenu dodajMenuNaMenu(JMenu zgornji, String naslov) {
		JMenu spodnji = new JMenu(naslov);
		zgornji.add(spodnji);
		return spodnji;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); // Prostor za morebitne polepšave
		if (source == menuBarvaIgralca1) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo igralca", platno.barva_igralca_1);
			if (barva != null) {
				platno.barva_igralca_1 = barva;
				platno.repaint();
			}
		}
		
		if (source == menuBarvaIgralca2) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo igralca", platno.barva_igralca_2);
			if (barva != null) {
				platno.barva_igralca_2 = barva;
				platno.repaint();
			}
		}
		
		if (source == menuOsnovnaIgra) {
			Okno okno = new Okno();
			okno.pack();
			okno.setVisible(true);
		}
		
		if (source == menuIgraProtiRacunalniku) {
			Okno okno = new Okno(15, 15, "1. igralec", "2. igralec", VrstaIgralca.C, VrstaIgralca.R);
			okno.pack();
			okno.setVisible(true);
			okno.platno.osnovni_meni = false;
			okno.osvezi_vmesnik();
		}
		
		if (source == menuIgraPoMeri) {
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
	    			    if (igralec1_ime == null) igralec1_ime = "1. igralec";
	    			    
	    			    String igralec2_ime = podatki.player_2.getText();
	    			    if (igralec2_ime == null) igralec2_ime = "2. igralec";
	    			    
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
	
	public void konec_igre(boolean je_zmaga) {
		if (je_zmaga) {
			igra.zamenjaj_igralca();
			
			JPanel okence = new JPanel();
			JOptionPane.showMessageDialog(okence, "Zmagal je " + igra.ime_igralca_na_potezi(), "Konec igre", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			JPanel okence = new JPanel();
			JOptionPane.showMessageDialog(okence, "Igra je neodločena!", "Konec igre", JOptionPane.PLAIN_MESSAGE);
		}
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void osvezi_vmesnik() {
		platno.repaint();
	}
	
	public void odigraj(Koordinati poteza) { // znebi se tega raka
		int[] kvadratek = new int[2];
		kvadratek[0] = poteza.getX();
		kvadratek[1] = poteza.getY();
		System.out.println("" + kvadratek[0] + " " + kvadratek[1]);
		int st_kvadratka = (kvadratek[1] + 1) * 16 + kvadratek[0] + 1;
 		if (platno.prvi) platno.izbrana_polja.get("1").add(st_kvadratka);
		else platno.izbrana_polja.get("2").add(st_kvadratka);
		platno.prvi = !platno.prvi;
	}
}