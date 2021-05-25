package logika;

import java.util.List;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import splosno.KdoIgra;
import splosno.Koordinati;

public class Igra {

	protected Polje[][] polje;
	final Igralec CRNI = Igralec.X;
	final Igralec BELI = Igralec.O;
	final Polje PRAZEN = Polje.PRAZEN;
	protected KdoIgra igralec_na_potezi;
	protected HashMap<String, Igralec> igralci;
	protected KdoIgra igralec1;
	protected KdoIgra igralec2;
	protected List<EnotaPolja> enote_polja;
	protected HashSet<Koordinati> mnozica_potez = new HashSet<Koordinati>();
	protected HashSet<Koordinati> mnozica_izvedenih_potez = new HashSet<Koordinati>();
	protected List<Koordinati> zmagovalna_poteza = new ArrayList<Koordinati>();
	
	public static void main(String[] args) throws Exception {
		Igra i = new Igra();
		i.odigraj(new Koordinati(0, 0));
		i.zamenjaj_igralca();
		i.odigraj(new Koordinati(1, 1));
		i.zamenjaj_igralca();
		i.odigraj(new Koordinati(2, 2));
		i.zamenjaj_igralca();
		i.odigraj(new Koordinati(3, 3));
		i.zamenjaj_igralca();
		i.odigraj(new Koordinati(4, 4));

		Igra ki = new Igra(i);
		ki.zamenjaj_igralca();
		ki.odigraj(new Koordinati(5, 5));
		ki.stanje_polja();
		
	}
	
	public enum Igralec {
		O, X;
		
		public Igralec nasprotnik() {
			return (this == X ? O : X);
		}
		
		public Polje getPolje() {
			return (this == X ? Polje.X : Polje.O);
		}
	}
	
	public enum Polje {
		O, X, PRAZEN;
		
		public Igralec getIgralec() {
			return (this == X ? Igralec.X : Igralec.O);
		}
		
		public String toString() {
			if (this == PRAZEN) return "prazen";
			return (this == X ? "X" : "O");
		}
	}
	
	public enum Stanje {
		ZMAGA_O, ZMAGA_X, NEODLOCENO, V_TEKU
	}

	public Igra(int x, int y, String igralec1_ime, String igralec2_ime) {
		polje = new Polje[y][x];
		igralci = new HashMap<String, Igralec>(2);
		igralec1 = new KdoIgra(igralec1_ime);
		igralec2 = new KdoIgra(igralec2_ime);
		igralci.put(igralec1.ime(), BELI);
		igralci.put(igralec2.ime(), CRNI);
		igralec_na_potezi = igralec1;
		// enote_polja = new ArrayList<EnotaPolja>();
		
		napolni_polje_s_praznimi();
		// zgradi_enote_polja();
		napolni_poteze();
	}
	
	public Igra() {
		this(15, 15, "1. igralec", "2. igralec");
	}
	
	public Igra(Igra igra) {
		this(igra.dimenzija_polja_x(),
				igra.dimenzija_polja_y(),
				igra.imena_igralcev()[0],
				igra.imena_igralcev()[1]);
		
		this.igralec_na_potezi = igra.igralec_na_potezi;
		this.polje = kopija_matrike(igra.polje);
		this.mnozica_potez = new HashSet<Koordinati>(igra.mnozica_potez);
		this.mnozica_izvedenih_potez = new HashSet<Koordinati>(igra.mnozica_izvedenih_potez);
	}
	
	private Polje[][] kopija_matrike(Polje[][] matrika) {
		int x = matrika[0].length;
		int y = matrika.length;
		Polje[][] nova = new Polje[y][x];
		for(int i = 0; i < nova.length; i++) {
			for(int j = 0; j < nova[i].length; j++) {				
				nova[i][j] = matrika[i][j];
			}
		}
		return nova;
	}
	
	public void napolni_polje_s_praznimi() {
		for (int j = 0; j < dimenzija_polja_y(); j++) {
			Polje[] prazna_vrstica = new Polje[dimenzija_polja_x()]; 
			Arrays.fill(prazna_vrstica, PRAZEN);
			polje[j] = prazna_vrstica;
		}
	}
	
	/*
	public void zgradi_enote_polja() { // Treba preveriti ali deluje pravilno (načeloma ja)
		zgradi_vrstice_ali_stolpce(true);
		zgradi_vrstice_ali_stolpce(false);
		zgradi_diagonale();
		zgradi_antidiagonale();
	}
	
	public void zgradi_vrstice_ali_stolpce(boolean je_vrstica) { 
		int x = (je_vrstica) ? dimenzija_polja_y() : dimenzija_polja_x();
		int y = (je_vrstica) ? dimenzija_polja_x() : dimenzija_polja_y();
		for (int j = 0; j < x; j++) {
			List<int[]> vrstica = new ArrayList<int[]>();
			for (int i = 0; i < y; i++) {
				int[] koordinati = new int[2];
				koordinati[0] = i;
				koordinati[1] = j;
				vrstica.add(koordinati);
			}
			enote_polja.add(new EnotaPolja(vrstica));
		}
	}
	
	public void zgradi_diagonale() { // Prostor za morebitne polepšave
		int x_dolzina = dimenzija_polja_x();
		int y_dolzina = dimenzija_polja_y();
		
		for (int odmik = 0; odmik < x_dolzina; odmik++) {
			List<int[]> diagonala = new ArrayList<int[]>();
			int x = 0 + odmik;
			int y = 0;
			while (je_veljavna_poteza(x, y)) {
				int[] koordinati = new int[2];
				koordinati[0] = x;
				koordinati[1] = y;
				diagonala.add(koordinati);
				x += 1;
				y += 1;
			}
			enote_polja.add(new EnotaPolja(diagonala));
		}
		
		for (int odmik = 1; odmik < y_dolzina; odmik++) {
			List<int[]> diagonala = new ArrayList<int[]>();
			int x = 0;
			int y = 0 + odmik;
			while (je_veljavna_poteza(x, y)) {
				int[] koordinati = new int[2];
				koordinati[0] = x;
				koordinati[1] = y;
				diagonala.add(koordinati);
				x += 1;
				y += 1;
			}
			enote_polja.add(new EnotaPolja(diagonala));
		}
	}
	
	public void zgradi_antidiagonale() { // Prostor za morebitne polepšave
		int x_dolzina = dimenzija_polja_x();
		int y_dolzina = dimenzija_polja_y();
		
		for (int odmik = 0; odmik < x_dolzina; odmik++) {
			List<int[]> diagonala = new ArrayList<int[]>();
			int x = x_dolzina - 1 - odmik;
			int y = y_dolzina - 1;
			while (je_veljavna_poteza(x, y)) {
				int[] koordinati = new int[2];
				koordinati[0] = x;
				koordinati[1] = y;
				diagonala.add(koordinati);
				x -= 1;
				y -= 1;
			}
			enote_polja.add(new EnotaPolja(diagonala));
		}
		
		for (int odmik = 1; odmik < y_dolzina; odmik++) {
			List<int[]> diagonala = new ArrayList<int[]>();
			int x = x_dolzina - 1;
			int y = y_dolzina - 1 - odmik;
			while (je_veljavna_poteza(x, y)) {
				int[] koordinati = new int[2];
				koordinati[0] = x;
				koordinati[1] = y;
				diagonala.add(koordinati);
				x -= 1;
				y -= 1;
			}
			enote_polja.add(new EnotaPolja(diagonala));
		}
	}
	*/
	
	
	public boolean odigraj(Koordinati koordinati) {
		int y_izbrani = koordinati.getY();
		int x_izbrani = koordinati.getX();
		if (!je_veljavna_poteza(x_izbrani, y_izbrani)) return false;
		Polje izbrano_mesto = polje[y_izbrani][x_izbrani];
		if (izbrano_mesto != PRAZEN) {
			
			return false;
		}
		else {
			Polje aktivna_crka = igralci.get(igralec_na_potezi.ime()).getPolje();
			polje[y_izbrani][x_izbrani] = aktivna_crka;
			Koordinati izbrani_koordinati = new Koordinati(x_izbrani, y_izbrani);
			mnozica_potez.remove(izbrani_koordinati);
			mnozica_izvedenih_potez.add(izbrani_koordinati);
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
		if (y_izbrani >= y_dolzina || x_izbrani >= x_dolzina || y_izbrani < 0 || x_izbrani < 0) return false;
		return true;
	}
	
	public void stanje_polja() {
		for (Polje[] vrstica : polje) {
			System.out.print('|');
			for (Polje vrednost : vrstica) {
				System.out.print("" + vrednost + '|');
			}
			System.out.println();
		}
	}
	
	public KdoIgra kdo_igra_simbol() {
		return igralec_na_potezi;
	}
	
	public void zamenjaj_igralca() {
		if (igralec_na_potezi.ime().equals(igralec1.ime())) {
			igralec_na_potezi = igralec2;
		}
		else igralec_na_potezi = igralec1;
	}
	
	public boolean je_konec_igre() {
		return (je_konec_igre_vodoravno(polje, false) ||
			je_konec_igre_navpicno() || 
			je_konec_igre_diagonalno1(polje, false) ||
			je_konec_igre_diagonalno2());
	}
	
	public boolean je_konec_igre_vodoravno(Polje[][] p, boolean navpicno) {
		int koord_vrstice = 0;
		int koord_stolpca = 0;
		Koordinati zacetne_koordinate = new Koordinati(0, 0);
		for (Polje[] vrstica : p) {
			Polje trenutna_vrednost = PRAZEN; // Neopredeljena vrednost
			int stevec = 1;
			for (Polje vrednost : vrstica) {
				if (vrednost == PRAZEN) {
					trenutna_vrednost = PRAZEN;
					koord_stolpca++;
					continue;
				}
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec >= 5) {
						zmagovalna_poteza(zacetne_koordinate, (!navpicno) ? "vodoravno" : "navpicno");
						return true;
					}
				}
				else {
					zacetne_koordinate = new Koordinati(koord_stolpca, koord_vrstice);
					trenutna_vrednost = vrednost;
					stevec = 1;
				}
				koord_stolpca++;
			}
			koord_vrstice++;
			koord_stolpca = 0;
		}
		return false;
	}
	
	private Polje[][] transponiraj(Polje[][] polje) {
		Polje[][] polje2 = kopija_matrike(polje);
        Polje[][] b = new Polje[polje2[0].length][polje2.length];
        for (int i = 0; i < polje2.length; i++) {
            for (int j = 0; j < polje2[0].length; j++) {
                b[j][i] = polje2[i][j];
            }
        }
    return b;
    }
	
	private Polje[][] zavrti90stopinj(Polje[][] polje2){
		int x = dimenzija_polja_x();
		int y = dimenzija_polja_y();
		Polje[][] nova_matrika = new Polje[x][y];
		
		for (int i = x - 1; i >= 0; i--) {
			Polje[] nova_vrstica = new Polje[y];
			for (int j = 0; j < y; j++) {
				nova_vrstica[j] = polje2[j][i];
			}
			nova_matrika[x - (i + 1)] = nova_vrstica;
		}
		
		return nova_matrika;
	}
	
	public boolean je_konec_igre_navpicno() {
		Polje[][] transponiranka = transponiraj(polje);
		return je_konec_igre_vodoravno(transponiranka, true);
	}
	
	public boolean je_konec_igre_diagonalno1(Polje[][] p, boolean zarotirana) { // Prostor za morebitne polepšave.
		int y_dolzina = p.length;
		int x_dolzina = p[0].length;
		Koordinati zacetne_koordinate = new Koordinati(0, 0);
		for (int odmik = 0; odmik < x_dolzina; odmik++) {
			int x = 0 + odmik;
			int y = 0;
			Polje vrednost = p[y][x];
			Polje trenutna_vrednost = PRAZEN; // Neopredeljena vrednost
			int stevec = 0;
			while ((!zarotirana) ? je_veljavna_poteza(x, y) : je_veljavna_poteza(y, x)) {
				vrednost = p[y][x];
				if (vrednost == PRAZEN) {
					x += 1;
					y += 1;
					trenutna_vrednost = PRAZEN;
					continue;
				}
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec >= 5) {
						zmagovalna_poteza(zacetne_koordinate, (zarotirana) ? "antidiagonalno" : "diagonalno");
						return true;
					}
				}
				else {
					trenutna_vrednost = vrednost;
					stevec = 1;
					zacetne_koordinate = new Koordinati(x, y);
				}
				x += 1;
				y += 1;
			}
		}
		
		
		for (int odmik = 0; odmik < y_dolzina; odmik++) {
			int x = 0;
			int y = 0 + odmik;
			Polje vrednost = p[y][x];
			Polje trenutna_vrednost = PRAZEN; // Neopredeljena vrednost
			int stevec = 0;
			while ((!zarotirana) ? je_veljavna_poteza(x, y) : je_veljavna_poteza(y, x)) {
				vrednost = p[y][x];
				if (vrednost == PRAZEN) {
					x += 1;
					y += 1;
					trenutna_vrednost = PRAZEN;
					continue;
				}
				if (vrednost == trenutna_vrednost) {
					stevec++;
					if (stevec >= 5) {
						zmagovalna_poteza(zacetne_koordinate, (zarotirana) ? "antidiagonalno" : "diagonalno");
						return true;
					}
				}
				else {
					trenutna_vrednost = vrednost;
					stevec = 1;
					zacetne_koordinate = new Koordinati(x, y);
				}
				x += 1;
				y += 1;
			}
		}
		
		
		return false;
		}

	public boolean je_konec_igre_diagonalno2() {
		Polje[][] zarotiranka = zavrti90stopinj(polje);
		return je_konec_igre_diagonalno1(zarotiranka, true);
	}
	
	public void zmagovalna_poteza(Koordinati zacetna, String smer) {
		int x = zacetna.getX();
		int y = zacetna.getY();
		int pomozni = x;
		System.out.println(zacetna);
		Polje tip = polje[y][x];
		
		if(smer.equals("navpicno") || smer.equals("antidiagonalno")) {
			tip = (smer.equals("navpicno")) ? polje[pomozni][y] : polje[pomozni][(dimenzija_polja_x() - 1) - y];
		}
		
		Polje nov_tip = tip;
		
		switch (smer) {
		case "vodoravno":
			while (nov_tip == tip && je_veljavna_poteza(x, y)) {
				zmagovalna_poteza.add(new Koordinati(x, y));
				x++;
				if (je_veljavna_poteza(x, y)) nov_tip = polje[y][x];
				else break;
			}
		
		case "navpicno":
			x = y;
			y = pomozni;
			while (nov_tip == tip && je_veljavna_poteza(x, y)) {
				zmagovalna_poteza.add(new Koordinati(x, y));
				y++;
				if (je_veljavna_poteza(x, y)) nov_tip = polje[y][x];
				else break;
			}
		
		case "diagonalno":
			while (nov_tip == tip && je_veljavna_poteza(x, y)) {
				zmagovalna_poteza.add(new Koordinati(x, y));
				x++;
				y++;
				if (je_veljavna_poteza(x, y)) nov_tip = polje[y][x];
				else break;
			}
		
		case "antidiagonalno":
			x = (dimenzija_polja_x() - 1) - y;
			y = pomozni;
			
			while (nov_tip == tip && je_veljavna_poteza(x, y)) {
				zmagovalna_poteza.add(new Koordinati(x, y));
				x--;
				y++;
				if (je_veljavna_poteza(x, y)) nov_tip = polje[y][x];
				else break;
			}
		}
	}

	
	public Stanje stanje() {
		if (je_konec_igre()) {
			return ((igralec_na_potezi == igralec1) ? Stanje.ZMAGA_X : Stanje.ZMAGA_O);
		}
		if (mnozica_potez.size() == 0) return Stanje.NEODLOCENO;
		return Stanje.V_TEKU;
	}
	
	public String[] imena_igralcev() {
		String[] imena = new String[2];
		imena[0] = igralec1.ime();
		imena[1] = igralec2.ime();
		return imena;
	}
	
	public String ime_igralca_na_potezi() {
		return igralec_na_potezi.ime();
	}
	
	public Igralec na_potezi() {
		return igralci.get(ime_igralca_na_potezi());
	}
	
	public void napolni_poteze() {
		for (int j = 0; j < dimenzija_polja_y(); j++) {
			for (int i = 0; i < dimenzija_polja_x(); i++) mnozica_potez.add(new Koordinati(i, j));
		}
	}
	
	public Polje[][] polje() {
		return polje;
	}
	
	public List<EnotaPolja> enote_polja() {
		return enote_polja;
	}
	
	public HashSet<Koordinati> mnozica_potez() {
		return mnozica_potez;
	}
	
	public HashSet<Koordinati> mnozica_izvedenih_potez() {
		return mnozica_izvedenih_potez;
	}
	
	public List<Koordinati> zmagovalna_poteza() {
		return zmagovalna_poteza;
	}

}



// [[1, 2, 0], [0, 0, 0], [2, 1, 0]]
		
/*		
|1|2|0| x = 3 - yr
|0|0|0| y = xr
|2|1|0|
|5|7|2| (1, 2) -> (1, 1)
*/ 

