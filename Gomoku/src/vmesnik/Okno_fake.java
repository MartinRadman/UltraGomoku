package vmesnik;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Okno_fake extends JFrame implements ActionListener {
	protected Platno platno;
	
	private JMenuItem menuOdpri;
	private JMenuItem menuShrani;
	private JMenuItem menuKoncaj;
	
	private JMenuItem menuPrazen;
	private JMenuItem menuCikel;
	private JMenuItem menuPoln;
	private JMenuItem menuPolnDvodelen;
	
	private JMenuItem menuBarvaPovezave, menuBarvaTocke, menuBarvaAktivneTocke, menuBarvaIzbraneTocke, menuBarvaDebelinaPovezave;
	
	public Okno_fake() {
		super();
		setTitle("Urejevalnik grafov");
		platno = new Platno(800, 800);
		add(platno);
		
		//menuji
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuDatoteka = dodajMenu(menubar, "Datoteka");
		JMenu menuGraf = dodajMenu(menubar, "Graf");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		
		
		//JMenuItem menuOdpri = new JMenuItem("Odpri ...");
		//menuDatoteka.add(menuOdpri);
		//menuOdpri.addActionListener(this);
		
		menuOdpri = dodajMenuItem(menuDatoteka, "Odpri ...");
		menuShrani = dodajMenuItem(menuDatoteka, "Shrani ...");
		menuKoncaj = dodajMenuItem(menuDatoteka, "Konèaj");
		
		menuPrazen = dodajMenuItem(menuGraf, "Prazen ...");
		menuCikel = dodajMenuItem(menuGraf, "Cikel ...");
		menuPoln = dodajMenuItem(menuGraf, "Poln ...");
		menuPolnDvodelen = dodajMenuItem(menuGraf, "Poln dvodelen ...");
		menuBarvaPovezave = dodajMenuItem(menuNastavitve, "Barva povezave ...");
		menuBarvaTocke = dodajMenuItem(menuNastavitve, "Barva toèke ...");
		menuBarvaAktivneTocke = dodajMenuItem(menuNastavitve, "Barva aktivne toèke ...");
		menuBarvaIzbraneTocke = dodajMenuItem(menuNastavitve, "Barva izbrane toèke ...");
		menuBarvaDebelinaPovezave = dodajMenuItem(menuNastavitve, "Debelina povezave ...");
	
	
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == menuOdpri) {
			JFileChooser dialog = new JFileChooser(); 
			int izbira = dialog.showOpenDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				// Graf g = Graf.preberi(ime);
				// platno.nastaviGraf(g); // komponenta k jo imamo na vrhu okna
			}
		}
		else if (source == menuShrani) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showSaveDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				try {
					platno.graf.shrani(ime);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (source == menuKoncaj) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if (source == menuPrazen) {
			String steviloTock = JOptionPane.showInputDialog(this, "Število toèk:");
			if (steviloTock != null && steviloTock.matches("\\d+")) {
				Graf g = Graf.prazen(Integer.parseInt(steviloTock));
				g.razporedi(400, 400, 300);
				platno.nastaviGraf(g);
			}
		}
		else if (source == menuCikel) {
			String steviloTock = JOptionPane.showInputDialog(this, "Število toèk:");
			if (steviloTock != null && steviloTock.matches("\\d+")) {
				Graf g = Graf.cikel(Integer.parseInt(steviloTock));
				g.razporedi(400, 400, 300);
				platno.nastaviGraf(g);
			}
		}
		else if (source == menuPoln) {
			String steviloTock = JOptionPane.showInputDialog(this, "Število toèk:");
			if (steviloTock != null && steviloTock.matches("\\d+")) {
				Graf g = Graf.poln(Integer.parseInt(steviloTock));
				g.razporedi(400, 400, 300);
				platno.nastaviGraf(g);
			}
		}
		else if (source == menuPolnDvodelen) {
			JTextField nn = new JTextField();
			JTextField mm = new JTextField();
			JComponent[] polja = {
					new JLabel("Vnesi N:" ), nn, 
					new JLabel("Vnesi M:"), mm
			};
			int izbira = JOptionPane.showConfirmDialog(this, polja, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (izbira == JOptionPane.OK_OPTION && nn.getText().matches("\\d+") && mm.getText().matches("\\d+") ) {
				Graf g = Graf.polnDvodelen(Integer.parseInt(nn.getText()), Integer.parseInt(mm.getText()));
				g.razporedi(400, 400, 300);
				platno.nastaviGraf(g);
			}
		}
		else if (source == menuBarvaPovezave) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo povezav", platno.barvaPovezave);
			if (barva != null) {
				platno.barvaPovezave = barva;
				platno.repaint();
			}
		}
		else if (source == menuBarvaTocke) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo toèk", platno.barvaTocke);
			if (barva != null) {
				platno.barvaTocke = barva;
				platno.repaint();
			}
		}
		else if (source == menuBarvaAktivneTocke) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo aktivne toèke", platno.barvaAktivneTocke);
			if (barva != null) {
				platno.barvaAktivneTocke = barva;
				platno.repaint();
			}
		}
		else if (source == menuBarvaIzbraneTocke) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo izbranih toèk", platno.barvaIzbraneTocke);
			if (barva != null) {
				platno.barvaIzbraneTocke = barva;
				platno.repaint();
			}
		}
		else if (source == menuBarvaDebelinaPovezave) {
			String debelina = JOptionPane.showInputDialog(this, "Debelina povezave:");
			if (debelina != null && debelina.matches("\\d+")) {				
				platno.debelinaPovezave = Float.parseFloat(debelina);
				platno.repaint();
				}
			}
		
	}
}
