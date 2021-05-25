package inteligenca;

import java.util.HashMap;
import java.util.HashSet;

import logika.Igra.Igralec;
import logika.Igra;
import splosno.Koordinati;

public class Minimax extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE;
	private static final int ZGUBA = -ZMAGA;
	private static final int NEODLOC = 0;
	
	private int globina;
	
	public HashMap<Koordinati, Integer> ocene = new HashMap<Koordinati, Integer>();
	
	public Minimax (int globina) {
		super("minimax globina " + globina);
		this.globina = globina;
	}
	

	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza = minimax(igra, this.globina, igra.na_potezi());
		izpis(ocene);
		return najboljsaPoteza.poteza;	
	}
	
	// vrne najboljso ocenjeno potezo z vidike igralca jaz
	public OcenjenaPoteza minimax(Igra igra, int globina, Igralec jaz) {
		OcenjenaPoteza najboljsaPoteza = null;
		HashSet<Koordinati> moznePoteze = igra.mnozica_potez();
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			if (p.equals(new Koordinati(3, 3))) {
				kopijaIgre.odigraj(p);
			}
			else {kopijaIgre.odigraj(p);}
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O: ocena = (jaz == Igralec.O ? ZMAGA : ZGUBA); break;
			case ZMAGA_X: ocena = (jaz == Igralec.X ? ZMAGA : ZGUBA); break;
			case NEODLOCENO: ocena = NEODLOC; break;
			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				// globina > 1
				else ocena = minimax(kopijaIgre, globina - 1, jaz).ocena;	
			}
			//ocene.put(p, ocena);
			if (najboljsaPoteza == null 
					// max, če je p moja poteza
					|| jaz == igra.na_potezi() && ocena > najboljsaPoteza.ocena
					// sicer min 
					|| jaz != igra.na_potezi() && ocena < najboljsaPoteza.ocena)
				najboljsaPoteza = new OcenjenaPoteza(p, ocena);		
		}
		return najboljsaPoteza;
	}
	
	
	public void izpis(HashMap<Koordinati, Integer> hm) {
		String niz = "";
		for (Koordinati k : hm.keySet()) {
			System.out.println(niz + k + " Ocena: " + hm.get(k)); 
		}
	}
	

}
