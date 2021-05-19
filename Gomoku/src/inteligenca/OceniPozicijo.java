package inteligenca;

import java.util.ArrayList;
import java.util.Collection;
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
		
		vrednost_polj = oceni_enoto(stolpec, k, jaz, igra, vrednost_polj);
		vrednost_polj = oceni_enoto(vrstica, k, jaz, igra, vrednost_polj);
		vrednost_polj = oceni_enoto(diagonala, k, jaz, igra, vrednost_polj);
		vrednost_polj = oceni_enoto(antidiagonala, k, jaz, igra, vrednost_polj);
		
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
	
	public static HashMap<Koordinati, Integer> oceni_enoto(List<int[]> enota, Koordinati k, Igralec jaz, Igra igra, HashMap<Koordinati, Integer> vrednost_polj) {
		Polje[][] polje = igra.polje();
		int x = k.getX();
		int y = k.getY();
		Igralec igralec = polje[y][x].getIgralec();
		int faktor = (jaz == igralec) ? 1 : -1;
		for (int[] koordinati : enota) {
			if (polje[koordinati[1]][koordinati[0]] == Polje.PRAZEN) {
				Koordinati koord = new Koordinati(x, y);
				int nova_ocena;
				switch (razdalja_polj(koord, k)) {
				case 1: nova_ocena = 16; break;
				case 2: nova_ocena = 8; break;
				case 3: nova_ocena = 4; break;
				case 4: nova_ocena = 2; break;
				default: nova_ocena = 0;
				}
				int ocena = vrednost_polj.getOrDefault(koord, 0) + nova_ocena * faktor;
				vrednost_polj.put(koord, ocena);
			}
		}
		return vrednost_polj;
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

