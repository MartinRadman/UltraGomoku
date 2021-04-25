package logika;

import java.util.HashMap;

import splosno.KdoIgra;
import splosno.Koordinati;

public class Igra {

	protected char[][] polje;
	final char BELI = 'X';
	final char CRNI = 'O';
	final char PRAZEN = '\u0000';
	protected KdoIgra igralec_na_potezi;
	protected HashMap<String, Character> igralci;
	protected KdoIgra igralec1;
	protected KdoIgra igralec2;
	
	public static void main(String[] args) throws Exception {
		Igra i = new Igra();
		i.odigraj(new Koordinati(0, 0));
		i.stanje_polja();
		System.out.print(i.je_konec_igre());
	}

	public Igra(int x, int y, String igralec1_ime, String igralec2_ime) {
		polje = new char[y][x];
		igralci = new HashMap<String, Character>(2);
		igralec1 = new KdoIgra(igralec1_ime);
		igralec2 = new KdoIgra(igralec2_ime);
		igralci.put(igralec1.ime(), BELI);
		igralci.put(igralec2.ime(), CRNI);
		igralec_na_potezi = igralec1;
	}
	
	public Igra() {
		this(15, 15, "1. igralec", "2. igralec");
	}
	
	public boolean odigraj(Koordinati koordinati) {
		int y_izbrani = koordinati.getY();
		int x_izbrani = koordinati.getX();
		if (!je_veljavna_poteza(x_izbrani, y_izbrani)) return false;
		
		char izbrano_mesto = polje[y_izbrani][x_izbrani];
		if (izbrano_mesto != PRAZEN) return false;
		else {
			char aktivna_crka = igralci.get(igralec_na_potezi.ime());
			polje[y_izbrani][x_izbrani] = aktivna_crka;
			zamenjaj_igralca();
			return true;
		}
	}
	
	public int dimenzija_polja_x() {
		return this.polje[0].length;
	}
	
	public int dimenzija_polja_y() {
		return this.polje.length;
	}
	
	public void spremeni_ime(KdoIgra igralec, String novo_ime) {
		igralec = new KdoIgra(novo_ime);
	}
	
	public boolean je_veljavna_poteza(int x_izbrani, int y_izbrani) {
		int y_dolzina = polje.length;
		int x_dolzina = polje[0].length;
		if (y_izbrani >= y_dolzina || x_izbrani >= x_dolzina) return false;
		return true;
	}
	
	public void stanje_polja() {
		for (char[] vrstica : polje) {
			System.out.print('|');
			for (char vrednost : vrstica) {
				System.out.print("" + vrednost + '|');
			}
			System.out.println();
		}
	}
	
	public KdoIgra kdo_igra_simbol() {
		return igralec_na_potezi;
	}
	
	public void zamenjaj_igralca() {
		if (igralec_na_potezi == igralec1) igralec_na_potezi = igralec2;
		else igralec_na_potezi = igralec1;
	}
	
	public boolean je_konec_igre() {
		return (je_konec_igre_vodoravno(polje) ||
			je_konec_igre_navpicno() || 
			je_konec_igre_diagonalno1(polje) ||
			je_konec_igre_diagonalno2());
	}
	
	public boolean je_konec_igre_vodoravno(char[][] p) {
		boolean kandidat = false; // Ali je trenutno zaporedje dolgo vsaj 5?
		for (char[] vrstica : p) {
			if (kandidat) return true;
			char trenutna_vrednost = 'N'; // Neopredeljena vrednost
			int stevec = 0;
			for (char vrednost : vrstica) {
				if (vrednost == PRAZEN) continue;
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec == 5) kandidat = true;
					if (stevec == 6) kandidat = false;
				}
				else {
					trenutna_vrednost = vrednost;
					stevec = 0;
					if (kandidat) return true;
				}
			}
		}
		return false;
	}
	
	public char[][] transponiraj(char[][] a) {
        char[][] b = new char[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[j][i] = a[i][j];
            }
        }
    return b;
    }
	
	public boolean je_konec_igre_navpicno() {
		char[][] transponiranka = transponiraj(polje);
		return je_konec_igre_vodoravno(transponiranka);
	}
	
	public boolean je_konec_igre_diagonalno1(char[][] p) { // Prostor za morebitne polepšave.
		int y_dolzina = p.length;
		int x_dolzina = p[0].length;
		boolean kandidat = false;
		for (int odmik = 0; odmik < x_dolzina; odmik++) {
			int x = 0 + odmik;
			int y = 0;
			if (kandidat) return true;
			char vrednost = p[y][x];
			char trenutna_vrednost = 'N'; // Neopredeljena vrednost
			int stevec = 0;
			while (je_veljavna_poteza(x, y)) {
				if (vrednost == PRAZEN) {
					x += 1;
					y += 1;
					continue;
				}
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec == 5) kandidat = true;
					if (stevec == 6) kandidat = false;
				}
				else {
					trenutna_vrednost = vrednost;
					stevec = 0;
					if (kandidat) return true;
				}
				x += 1;
				y += 1;
			}
		}
		
		
		for (int odmik = 0; odmik < y_dolzina; odmik++) {
			int x = 0;
			int y = 0 + odmik;
			if (kandidat) return true;
			char vrednost = p[y][x];
			char trenutna_vrednost = 'N'; // Neopredeljena vrednost
			int stevec = 0;
			while (je_veljavna_poteza(x, y)) {
				if (vrednost == PRAZEN) {
					x += 1;
					y += 1;
					continue;
				}
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec == 5) kandidat = true;
					if (stevec == 6) kandidat = false;
				}
				else {
					trenutna_vrednost = vrednost;
					stevec = 0;
					if (kandidat) return true;
				}
				x += 1;
				y += 1;
			}
		}
		
		
		return false;
		}

	public boolean je_konec_igre_diagonalno2() {
		char[][] transponiranka = transponiraj(polje);
		return je_konec_igre_diagonalno1(transponiranka);
	}
	
	public String[] imena_igralcev() {
		String[] imena = new String[2];
		imena[0] = igralec1.ime();
		imena[1] = igralec2.ime();
		return imena;
	}
}



// [[1, 2, 0], [0, 0, 0], [2, 1, 0]]
		
/*		
|1|2|0|
|0|0|0|
|2|1|0|
*/

