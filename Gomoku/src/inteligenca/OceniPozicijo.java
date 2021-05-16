package inteligenca;

import logika.Igra;
import logika.Igra.Igralec;
import logika.Igra.Polje;

public class OceniPozicijo {
	
	public enum Smer {
		VRSTICA, STOLPEC, DIAGONALA, ANTIDIAGONALA
	}
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (Vrsta v : Igra.VRSTE) {
			ocena = ocena + oceniVrsto(v, igra, jaz);
		}
		return ocena;	
	}
	
	public static Polje[] zgradi_enoto(Smer smer, int[] zacetek)
	
	public static int oceni_enoto (Smer smer, int[] zacetek, Igra igra, Igralec jaz) {
		Polje[][] plosca = igra.polje();
		
	}
	

}
