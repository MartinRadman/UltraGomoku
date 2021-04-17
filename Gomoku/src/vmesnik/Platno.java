package vmesnik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import logika.Igra;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	protected Color barva_igralca_1 = Color.BLACK;
	protected Color barva_igralca_2 = Color.RED;
	protected Color barva_ozadja = Color.GRAY;
	protected Igra igra;
	protected HashSet<int[]> kvadratki;
	protected int stranica_kvadratka;
	protected boolean prvi = true;
	protected HashMap<String, List<int[]>> izbrana_polja;

	public Platno(int sirina, int visina, Igra igra) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		this.igra = igra;
	    this.izbrana_polja = new HashMap<String, List<int[]>>();
	    izbrana_polja.put("1", new ArrayList<int[]>());
	    izbrana_polja.put("2", new ArrayList<int[]>());
	    this.kvadratki = new HashSet<int[]>();
	    
	    addMouseListener(this); //s tem povemo da se bo objekt odzival na misko
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true); //platno sposobno prejet fokus in bo reagiralo na dogodke s tipkovnico
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		
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
					if (j == 0) continue;
					String vrednost = "" + j;
					g.drawString(vrednost, (int) Math.round((j + 0.4) * stranica_kvadratka + prazno_obmocje_x / 2)
							, (int) Math.round((i  + 0.8) * stranica_kvadratka + prazno_obmocje_y / 2));
				}
				else{
					if (j == 0) {
						String vrednost = Character.toString((char) (('A' + i) - 1));
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
						this.kvadratki.add(nova_tabela);
					}
				}
			}
		}
		
		if (this.izbrana_polja.get("1") != null) {
			for (int[] izbranec : this.izbrana_polja.get("1")) {  // Prostor za morebitne polepšave. 
				int x = izbranec[0];
				int y = izbranec[1];
			
				g.setColor(barva_igralca_1);
				g.fillOval(x, y, (int) Math.round(0.8 * (stranica_kvadratka / 2)),
						(int) Math.round(0.8 * (stranica_kvadratka / 2)));
			}
		}
		
		if (this.izbrana_polja.get("2") != null) {                     // posodobi dinamiènost krogov, centriraj kroge
			for (int[] izbranec : this.izbrana_polja.get("2")) {
				int x = izbranec[0];
				int y = izbranec[1];
			
				g.setColor(barva_igralca_2);
				g.fillOval(x, y, (int) Math.round(0.8 * (stranica_kvadratka / 2)),
						(int) Math.round(0.8 * (stranica_kvadratka / 2)));
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.print("" + x + " " + y);
		for (int[] kvadratek : kvadratki) {
			if (Math.abs(kvadratek[0] - x) < (stranica_kvadratka / 2) &&
					Math.abs(kvadratek[1] - y) < (stranica_kvadratka / 2)) {
				if (prvi) this.izbrana_polja.get("1").add(kvadratek);
				else this.izbrana_polja.get("2").add(kvadratek);
			}
		}
		this.igra.zamenjaj_igralca();
		this.prvi = !prvi;
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