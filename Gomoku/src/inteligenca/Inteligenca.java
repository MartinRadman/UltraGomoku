package inteligenca;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import logika.Igra;
import logika.Igra.Igralec;

import splosno.Koordinati;


public class Inteligenca extends splosno.KdoIgra {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	final int casovna_omejitev = 4800; // v milisekundah
	final double c = Math.sqrt(2);
	protected long start = System.currentTimeMillis();
	protected long konec = start + casovna_omejitev;
	
	private int globina;
	
	public Inteligenca (int globina) {
		super("Martin Radman in Gašper Petrovič");
		this.globina = globina;
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		// Na začetku alpha = ZGUBA in beta = ZMAGA
		return alphabetaPoteze(igra, this.globina, ZGUBA, ZMAGA, igra.na_potezi()).poteza;
	}
	
	public static OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
		if (igra.na_potezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		HashSet<Koordinati> mnozica_potez = igra.mnozica_potez();
		List<Koordinati> moznePoteze = new ArrayList<Koordinati>();	
		for (Koordinati poteza : mnozica_potez) {
			moznePoteze.add(poteza);
		}
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremini vrednost kanditata. Zato ne more biti null.
		
		for (Koordinati p: moznePoteze) {
			boolean preskoci = true;
			for (Koordinati odigran : igra.mnozica_izvedenih_potez()) {
				if (igra.mnozica_izvedenih_potez().size() < 4 && je_osamljen(odigran, igra)) continue;
				if (ga_gledamo(p, odigran)) preskoci = false;				
			}
			if (preskoci) continue;
			
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj (p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O: ocenap = (jaz == Igralec.O ? ZMAGA : ZGUBA); break;
			case ZMAGA_X: ocenap = (jaz == Igralec.X ? ZMAGA : ZGUBA); break;
			case NEODLOCENO: ocenap = NEODLOC; break;
			default:
				// Nekdo je na potezi
				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
			}
			if (igra.na_potezi() == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha, ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				return new OcenjenaPoteza (kandidat, ocena);
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}
	

	public static boolean ga_gledamo(Koordinati p, Koordinati odigran) {
		int razdalja = Math.abs(p.getX() - odigran.getX()) + Math.abs(p.getY() - odigran.getY());
		if (razdalja < 2 && razdalja != 0) {
			return true;
		}
		if (Math.abs(p.getX() - odigran.getX()) == 1 && Math.abs(p.getY() - odigran.getY()) == 1) return true;
		return false;
		
	}
	
	public static boolean je_osamljen(Koordinati p, Igra igra) {
		for (Koordinati odigran : igra.mnozica_izvedenih_potez()) {
			if (ga_gledamo(p, odigran)) return true;
		}
		return false;
		
	}
	
}
