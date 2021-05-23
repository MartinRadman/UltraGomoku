package inteligenca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import logika.Igra;
import logika.Igra.Igralec;
import logika.Igra.Polje;
import splosno.Koordinati;

public class OceniPozicijo {
	
	final int RADIJ = 4;
	
	public enum Smer {
		VRSTICA, STOLPEC, DIAGONALA, ANTIDIAGONALA
	}
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		HashMap<Koordinati, Integer> vrednost_polj = new HashMap<Koordinati, Integer>();
		int ocena = 0;
		HashSet<Koordinati> mnozica_izvedenih_potez = igra.mnozica_izvedenih_potez();
		for (Koordinati k : mnozica_izvedenih_potez) {
			ocena = ocena + oceni_okolico(k, igra, jaz, vrednost_polj);
		}
		return ocena;	
	}
	
	public static int oceni_okolico(Koordinati k, Igra igra, Igralec jaz, HashMap<Koordinati, Integer> vrednost_polj) {
		int x = k.getX();
		int y = k.getY();
		List<int[]> stolpec = stolpec(igra, x, y);
		List<int[]> vrstica = vrstica(igra, x, y);
		List<int[]> diagonala = diagonala(igra, x, y);
		List<int[]> antidiagonala = antidiagonala(igra, x, y);
		
		vrednost_polj = oceni_enoto(stolpec, k, jaz, igra, vrednost_polj, false);
		vrednost_polj = oceni_enoto(vrstica, k, jaz, igra, vrednost_polj, false);
		vrednost_polj = oceni_enoto(diagonala, k, jaz, igra, vrednost_polj, false);
		vrednost_polj = oceni_enoto(antidiagonala, k, jaz, igra, vrednost_polj, false);
		
		return sum(vrednost_polj.values());	
	}
	
	public static List<int[]> stolpec(Igra igra, int x, int y) { 
		List<int[]> stolpec = new ArrayList<int[]>();
		for (int j = y - 4; j <= y + 4; j++) {
			if (igra.je_veljavna_poteza(x, j)) {
				int[] koordinati = new int[2];
				koordinati[0] = x;
				koordinati[1] = j;
				stolpec.add(koordinati);
			}
		}
		return stolpec;
	}
	
	public static List<int[]> vrstica(Igra igra, int x, int y) { // Prostor za morebitne polepšave
		List<int[]> vrstica = new ArrayList<int[]>();
		for (int i = x - 4; i <= x + 4; i++) {
			if (igra.je_veljavna_poteza(i, y)) {
				int[] koordinati = new int[2];
				koordinati[0] = i;
				koordinati[1] = y;
				vrstica.add(koordinati);
			}
		}
		return vrstica;
	}
	
	public static List<int[]> diagonala(Igra igra, int x, int y) { 
		List<int[]> diagonala = new ArrayList<int[]>();
		for (int i = - 4; i <= 4; i++) {
			if (igra.je_veljavna_poteza(x + i, y + i)) {
				int[] koordinati = new int[2];
				koordinati[0] = x + i;
				koordinati[1] = y + i;
				diagonala.add(koordinati);
			}
		}
		return diagonala;
	}
	
	public static List<int[]> antidiagonala(Igra igra, int x, int y) { // Prostor za morebitne polepšave
		List<int[]> antidiagonala = new ArrayList<int[]>();
		for (int i = - 4; i <= 4; i++) {
			if (igra.je_veljavna_poteza(x + i, y - i)) {
				int[] koordinati = new int[2];
				koordinati[0] = x + i;
				koordinati[1] = y - i;
				antidiagonala.add(koordinati);
			}
		}
		return antidiagonala;
	}
	
	public static HashMap<Koordinati, Integer> oceni_enoto(List<int[]> enota, Koordinati k, Igralec jaz, Igra igra, HashMap<Koordinati, Integer> vrednost_polj, boolean druga) {
		Polje[][] polje = igra.polje();
		int x = k.getX();
		int y = k.getY();
		Igralec igralec = polje[y][x].getIgralec();
		int faktor_desni= 1;
		int stevec = 0;
		boolean desna = false;
		for (int[] koordinati : enota) {
			stevec++;
			if (stevec == 5) {
				desna = true;
				break;
			}
			if (polje[koordinati[1]][koordinati[0]] != Polje.PRAZEN) {
				Koordinati koord = new Koordinati(x, y);
				if (!desna) {
					switch (razdalja_polj(koord, k)) {
					case 1: 
						switch (faktor_desni) {
						case 32: faktor_desni = 2000;
						case 24: faktor_desni = 700;
						case 12: faktor_desni = 300;
						case 8: faktor_desni = 140;
						case 6: faktor_desni = 64;
						case 4: faktor_desni = 28;
						case 2: faktor_desni = 20;
						default: faktor_desni = 16;
					    }
					break;
					
					case 2: 
						switch (faktor_desni) {
						case 6: faktor_desni = 32; break;
						case 4: faktor_desni = 24; break;
						case 2: faktor_desni = 12;
						default: faktor_desni = 8; break;
						}
				    break;
					
					case 3: if (faktor_desni == 2) faktor_desni = 6; else faktor_desni = 4; break;
					case 4: faktor_desni = 2; break;
					default: faktor_desni = 1;
					}
				}	
			}
		}
		for (int[] koordinati : enota) {
			if (polje[koordinati[1]][koordinati[0]] == Polje.PRAZEN) {
				Koordinati koord = new Koordinati(koordinati[0], koordinati[1]);
				int nova_ocena;
				switch (razdalja_polj(koord, k)) {
				case 1: nova_ocena = 16; break;
				case 2: nova_ocena = 8; break;
				case 3: nova_ocena = 4; break;
				case 4: nova_ocena = 2; break;
				default: nova_ocena = 0;
				}
				int ocena = vrednost_polj.getOrDefault(koord, 0) + nova_ocena * faktor_desni;
				vrednost_polj.put(koord, ocena);
			}
		}
		Collections.reverse(enota);
		if (!druga) return oceni_enoto(enota, k, jaz, igra, vrednost_polj, true);
		else return vrednost_polj;
	}
	
	private static int razdalja_polj(Koordinati a, Koordinati b) {
		int a0 = a.getX();
		int a1 = a.getY();
		int b0 = b.getX();
		int b1 = b.getY();
		int x_razdalja = Math.abs(a0 - b0);
		if (x_razdalja == 0) return Math.abs(a1 - b1);
		return x_razdalja;
	}
	
	private static int sum(Collection<Integer> c) {
		int vsota = 0;
		for (int x : c) {
			vsota += x;
		}
		return vsota;
	}
	

}



