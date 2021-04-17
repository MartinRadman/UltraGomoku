package vmesnik;

import javax.swing.JFrame;

import logika.Igra;

@SuppressWarnings("serial")
public class Okno extends JFrame {
	protected Platno platno;
	
	public static void main(String[] args) throws Exception {
		new Okno();
	}
	
	public Okno() {
		super();
		setTitle("Gomoku");
		Igra igra = new Igra();
		platno = new Platno(800, 800, igra);
		add(platno);
	}

}
