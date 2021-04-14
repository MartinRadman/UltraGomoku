package vmesnik;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Okno extends JFrame {
	protected Platno platno;
	
	public static void main(String[] args) throws Exception {
		new Okno();
	}
	
	public Okno() {
		super();
		setTitle("Gomoku");
		platno = new Platno(800, 800);
		add(platno);
	}

}
