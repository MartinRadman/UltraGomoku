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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logika.Igra;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	protected Platno platno;
	
	private Igra igra;
	
	protected String ime_igralca_1;
	protected String ime_igralca_2;
	
	private JMenuItem menuBarvaIgralca1;
	private JMenuItem menuBarvaIgralca2;
	private JMenuItem menuOsnovnaIgra;
	private JMenuItem menuIgraPoMeri;
	
	public Okno(int sirina_igralnega_polja, int visina_igralnega_polja, String igralec1_ime, String igralec2_ime) {
		super();
		setTitle("Gomoku");
		igra = new Igra(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime);
		platno = new Platno(800, 800, this, igra);
		add(platno);
		
		String[] imena_igralcev = igra.imena_igralcev();
		ime_igralca_1 = imena_igralcev[0];
		ime_igralca_2 = imena_igralcev[1];
		
		//menuji
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuNovaIgra = dodajMenuNaMenu(menuIgra, "Nova igra");
		
		menuOsnovnaIgra = dodajMenuItem(menuNovaIgra, "Nova osnovna igra");
		menuIgraPoMeri = dodajMenuItem(menuNovaIgra, "Nova igra po meri ...");
		
		JMenu menuPrilagoditve = dodajMenu(menubar, "Prilagoditve");
		JMenu menuBarvaIgralcev = dodajMenuNaMenu(menuPrilagoditve, "Spremeni barvo igralcev ...");
		
		menuBarvaIgralca1 = dodajMenuItem(menuBarvaIgralcev, "Barva igralca " + ime_igralca_1 + " ..."); // Treba je popraviti izpis imenov igralcev
		menuBarvaIgralca2 = dodajMenuItem(menuBarvaIgralcev, "Barva igralca " + ime_igralca_2 + " ...");
	
		
	}
	
	public Okno() {
		this(15, 15, "1. igralec", "2. igralec");
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
		
		if (source == menuIgraPoMeri) { // Treba je popraviti okence
			JTextField poljeVrstice = new JTextField(5);
		    JTextField poljeStolpci = new JTextField(5);
		    JTextField poljeIme1 = new JTextField(5);
		    JTextField poljeIme2 = new JTextField(5);

		    JPanel okence = new JPanel();
		    okence.add(new JLabel("Število vrstic:"));
		    okence.add(poljeVrstice);
		    okence.add(Box.createHorizontalStrut(15)); // presledek
		    okence.add(new JLabel("Število stolpcev:"));
		    okence.add(poljeStolpci);
		    okence.add(Box.createRigidArea(new Dimension(1, 1)));
		    okence.add(new JLabel("Ime 1. igralca"));
		    okence.add(poljeIme1);
		    okence.add(new JLabel("Ime 2. igralca"));
		    okence.add(poljeIme2);

		    int rezultat = JOptionPane.showConfirmDialog(null, okence,
		            "Prosimo vnesite zahtevane podatke", JOptionPane.OK_CANCEL_OPTION);
		    
		    if (rezultat == JOptionPane.OK_OPTION) {
		    	int sirina_igralnega_polja = Integer.valueOf(poljeStolpci.getText());
		    	int visina_igralnega_polja = Integer.valueOf(poljeVrstice.getText());
		    	String igralec1_ime = poljeIme1.getText();
		    	String igralec2_ime = poljeIme2.getText();
		    	Okno okno = new Okno(sirina_igralnega_polja, visina_igralnega_polja, igralec1_ime, igralec2_ime);
				okno.pack();
				okno.setVisible(true);
		    }
		    
		 
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
		igra = new Igra();
		
	}
}