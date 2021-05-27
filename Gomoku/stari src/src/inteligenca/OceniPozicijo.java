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
	 
	final static int vse_prazno = 1;
	final static int eden_poln_4 = 2;
	final static int eden_poln_3 = 8;
	final static int eden_poln_2 = 32;
	final static int eden_poln_1 = 2048;
	final static int dva_polna_3_4 = 26;
	final static int dva_polna_2_4 = 1022;
	final static int dva_polna_1_4 = 1023;
	final static int dva_polna_1_3 = 1024;
	final static int dva_polna_2_3 = 1025;
	final static int dva_polna_1_2 = 16384;
	final static int trije_polni_2_3_4 = 10000;
	final static int trije_polni_1_3_4 = 10000;
	final static int trije_polni_1_2_4 = 10000;
	final static int trije_polni_1_2_3 = 100000;
	final static int open_stiri = 1000000;
	final static int vsi_polni = -1;
	
	
	/*
	final static int vse_prazno = 1; 
	final static int eden_poln_4 = 2;
	final static int eden_poln_3 = 3;
	final static int eden_poln_2 = 5;
	final static int eden_poln_1 = 11;
	final static int dva_polna_3_4 = 4;
	final static int dva_polna_2_4 = 10;
	final static int dva_polna_1_4 = 10;
	final static int dva_polna_2_3 = 10;
	final static int dva_polna_1_3 = 10;
	final static int dva_polna_1_2 = 14;
	final static int trije_polni_2_3_4 = 16;
	final static int trije_polni_1_3_4 = 16;
	final static int trije_polni_1_2_4 = 16;
	final static int trije_polni_1_2_3 = 20;
	final static int vsi_polni = -1;
	*/
	
	static boolean maksimalna_vrednost = false;
	static boolean minimalna_vrednost = false;
	
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
		int faktor_igralec = (igralec == jaz) ? 1 : -1;
		int faktor_desni= 1;
		boolean desna = false;
		for (int[] koordinati : enota) {
			Koordinati koord = new Koordinati(koordinati[0], koordinati[1]);
			if (koord.equals(k)) {
				desna = true;
				break;
			}
			int mesto_od_k = enota.indexOf(k);
			
			Polje tip_igralca = polje[koordinati[1]][koordinati[0]];
			if (tip_igralca != Polje.PRAZEN) {
				if (tip_igralca.getIgralec() != igralec) continue;
				if (!desna) {
					switch (razdalja_polj(koord, k)) {
					case 1: 
						switch (faktor_desni) {
						case trije_polni_2_3_4:  faktor_desni = vsi_polni; break;
						case dva_polna_2_3: 
							
							if (enota.size() > mesto_od_k + 1) {
								int x_kandidata = enota.get(mesto_od_k + 1)[0];
								int y_kandidata = enota.get(mesto_od_k + 1)[1];
								faktor_desni = (polje[y_kandidata][x_kandidata] == Polje.PRAZEN) ? open_stiri : trije_polni_1_2_3;
							}
							else {
								faktor_desni = trije_polni_1_2_3;
							}

							break;
							
							
						case dva_polna_2_4: faktor_desni = trije_polni_1_2_4; break;
						case dva_polna_3_4: faktor_desni = trije_polni_1_3_4; break;
						case eden_poln_2: if (faktor_desni == trije_polni_1_2_3) System.out.println("klee"); faktor_desni = dva_polna_1_2; break;
						case eden_poln_3: faktor_desni = dva_polna_1_3; break;
						case eden_poln_4: faktor_desni = dva_polna_1_4; break;
						default: if (faktor_desni == trije_polni_1_2_3) System.out.println("kle1"); faktor_desni = eden_poln_1; break;
					    }
					break;
					case 2:
						switch (faktor_desni) {
						case dva_polna_3_4: faktor_desni = trije_polni_2_3_4; break; 
						case eden_poln_3: faktor_desni = dva_polna_2_3; break;
						case eden_poln_4: faktor_desni = dva_polna_2_4; break;
						default: if (faktor_desni == trije_polni_1_2_3) System.out.println("kle2"); faktor_desni = eden_poln_2; break;
						}
				    break;
					
					case 3: if (faktor_desni == trije_polni_1_2_3) System.out.println("kle11"); if (faktor_desni == eden_poln_4) faktor_desni = dva_polna_3_4; else faktor_desni = eden_poln_3; break;
					case 4: if (faktor_desni == trije_polni_1_2_3) System.out.println("kle10"); faktor_desni = eden_poln_4; break;
					default: if (faktor_desni == trije_polni_1_2_3) System.out.println("kle3"); faktor_desni = vse_prazno; break;
					}
				}	
			}
		}
		for (int[] koordinati : enota) {
			Koordinati koord = new Koordinati(koordinati[0], koordinati[1]);
			if (koord.equals(k)) {
				desna = true;
				break;
			}
			if (polje[koordinati[1]][koordinati[0]] == Polje.PRAZEN) {
				int nova_ocena;
				switch (razdalja_polj(koord, k)) {
				case 1: nova_ocena = 16; break;
				case 2: nova_ocena = 8; break;
				case 3: nova_ocena = 4; break;
				case 4: nova_ocena = 2; break;
				default: nova_ocena = 0;
				}
				int ocena = (vrednost_polj.getOrDefault(koord, 0) + nova_ocena * faktor_desni) * faktor_igralec;
				if (k.equals(new Koordinati(8, 9))) {
					int a = 5;
				}
				//System.out.println(vrednost_polj.getOrDefault(koord, 0));
				if (faktor_desni == -1) {
					if (igralec == jaz) maksimalna_vrednost = true; else minimalna_vrednost = true; 
				}
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
	
	public static int[] najdi_petega(List<int[]> enota, Igra igra) {
		String vrsta = najdi_vrsto_enote(enota);
		int[] cetrti = enota.get(0);
		int cetrtix = cetrti[0];
		int cetrtiy = cetrti[1];
		switch (vrsta) {
		case "navzgor": cetrtiy += 1; break;
		case "navzdol": cetrtiy -= 1; break;
		case "levo": cetrtix += 1; break;
		case "desno": cetrtix -= 1; break;
		case "navzdol_desno": cetrtiy -= 1; cetrtix -= 1; break;
		case "navzdol_levo":cetrtiy -= 1; cetrtix += 1; break;
		case "navzgor_desno": cetrtiy += 1; cetrtix -= 1; break;
		case "navzgor_levo": cetrtiy += 1; cetrtix += 1; break;
		}
		
		int petix = cetrtix;
		int petiy = cetrtiy;
		if (igra.je_veljavna_poteza(petix, petiy)) {
			int[] peti = new int[2];
			peti[0] = petix;
			peti[1] = petiy;
			return peti;
		}
		else {
			int[] peti = new int[2];
			peti[0] = -1;
			peti[1] = -1;
			return peti;
		}
	}
	
	public static String najdi_vrsto_enote(List<int[]> enota) {
		int[] e1 = enota.get(0);
		int[] e2 = enota.get(1);
		
		int e1x = e1[0];
		int e1y = e1[1];
		int e2x = e2[0];
		int e2y = e2[1];
		
		if (e1x - e2x == 0) {
			if(e1y > e2y) return "navzgor";
			else return "navzdol";
		}
		
		if (e1y - e2y == 0) {
			if(e1x > e2x) return "levo";
			else return "desno";
		}
		
		if (e2x > e1x) {
			if (e2y > e1y) return "navzdol_desno";
			else return "navzgor_desno";
		}
		
		
			if (e2y > e1y) return "navzdol_levo";
			else return "navzgor_levo";
		
	}
	

}



