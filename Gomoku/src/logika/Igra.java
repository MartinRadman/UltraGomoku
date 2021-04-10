package logika;

import java.util.HashMap;

public class Igra {

	protected static char[][] polje;
	final char BELI = 'B';
	final char CRNI = 'C';
	final static char PRAZEN = '\u0000';
	protected static String igralec_na_potezi;
	protected static HashMap<String, Character> igralci;
	protected static String igralec1;
	protected static String igralec2;
	
	public static void main(String[] args) throws Exception {
		new Igra("Lojze", "Ne vem, zmisl se neki, ne morm se spomnt");
		odigraj(new Koordinati(0, 0));
		stanje_polja();
		System.out.print(je_konec_igre());

	}

	public Igra(int x, int y, String ime1, String ime2) {
		polje = new char[y][x];
		igralci = new HashMap<String, Character>(2);
		igralec1 = ime1;
		igralec2 = ime2;
		igralci.put(igralec1, BELI);
		igralci.put(igralec2, CRNI);
		igralec_na_potezi = ime1;
	}
	
	public Igra(String ime1, String ime2) {
		polje = new char[15][15];
		igralci = new HashMap<String, Character>(2);
		igralec1 = ime1;
		igralec2 = ime2;
		igralci.put(igralec1, BELI);
		igralci.put(igralec2, CRNI);
		igralec_na_potezi = ime1;
	}
	
	public static boolean odigraj(Koordinati koordinati) {
		int y_izbrani = koordinati.y;
		int x_izbrani = koordinati.x;
		if (!je_veljavna_poteza(x_izbrani, y_izbrani)) return false;
		
		char izbrano_mesto = polje[y_izbrani][x_izbrani];
		if (izbrano_mesto != PRAZEN) return false;
		else {
			char aktivna_crka = igralci.get(igralec_na_potezi);
			polje[y_izbrani][x_izbrani] = aktivna_crka;
			zamenjaj_igralca();
			return true;
		}
	}
	
	public static boolean je_veljavna_poteza(int x_izbrani, int y_izbrani) {
		int y_dolzina = polje.length;
		int x_dolzina = polje[0].length;
		if (y_izbrani >= y_dolzina || x_izbrani >= x_dolzina) return false;
		return true;
	}
	
	public static void stanje_polja() {
		for (char[] vrstica : polje) {
			System.out.print('|');
			for (char vrednost : vrstica) {
				System.out.print("" + vrednost + '|');
			}
			System.out.println();
		}
	}
	
	public static void zamenjaj_igralca() {
		if (igralec_na_potezi == igralec1) igralec_na_potezi = igralec2;
		else igralec_na_potezi = igralec1;
	}
	
	public static boolean je_konec_igre() { // Treba je še izločiti fake null iz preverjanja.
		if (je_konec_igre_vodoravno(polje) ||
			je_konec_igre_navpicno() || 
			je_konec_igre_diagonalno1(polje) ||
			je_konec_igre_diagonalno2()) return true;
		else return false;
	}
	
	public static boolean je_konec_igre_vodoravno(char[][] p) {
		boolean kandidat = false; // Ali je trenutno zaporedje dolgo vsaj 5?
		for (char[] vrstica : p) {
			if (kandidat) return true;
			char trenutna_vrednost = 'N'; // Neopredeljena vrednost
			int stevec = 0;
			for (char vrednost : vrstica) {
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
	
	public static char[][] transponiraj(char[][] a) {
        char[][] b = new char[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[j][i] = a[i][j];
            }
        }
    return b;
    }
	
	public static boolean je_konec_igre_navpicno() {
		char[][] transponiranka = transponiraj(polje);
		return je_konec_igre_vodoravno(transponiranka);
	}
	
	public static boolean je_konec_igre_diagonalno1(char[][] p) {
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

	public static boolean je_konec_igre_diagonalno2() {
		char[][] transponiranka = transponiraj(polje);
		return je_konec_igre_diagonalno1(transponiranka);
	}
}



// [[1, 2, 0], [0, 0, 0], [2, 1, 0]]
		
/*		
|1|2|0|
|0|0|0|
|2|1|0|
*/

