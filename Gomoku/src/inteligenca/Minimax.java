package inteligenca;

import java.util.List;

import logika.Igra.Igralec;
import inteligenca_fake.OceniPozicijo;
import inteligenca.OcenjenaPoteza;
import logika.Igra;
import splosno.Koordinati;

public class Minimax extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE;
	private static final int ZGUBA = -ZMAGA;
	private static final int NEODLOC = 0;
	
	private int globina;
	
	public Minimax (int globina) {
		super("minimax globina " + globina);
		this.globina = globina;
	}
	
	@Override
	public Koordinati izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza = minimax(igra, this.globina, igra.na_potezi());
		return najboljsaPoteza.poteza;	
	}
	
	// vrne najboljso ocenjeno potezo z vidike igralca jaz
	public OcenjenaPoteza minimax(Igra igra, int globina, Igralec jaz) {
		OcenjenaPoteza najboljsaPoteza = null;
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj (p);
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O: ocena = (jaz == Igralec.O ? ZMAGA : ZGUBA); break;
			case ZMAGA_X: ocena = (jaz == Igralec.X ? ZMAGA : ZGUBA); break;
			case NEODLOCENO: ocena = NEODLOC; break;
			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				// globina > 1
				else ocena = minimax(kopijaIgre, globina-1, jaz).ocena;	
			}
			if (najboljsaPoteza == null 
					// max, Ä?e je p moja poteza
					|| jaz == igra.naPotezi() && ocena > najboljsaPoteza.ocena
					// sicer min 
					|| jaz != igra.naPotezi() && ocena < najboljsaPoteza.ocena)
				najboljsaPoteza = new OcenjenaPoteza (p, ocena);		
		}
		return najboljsaPoteza;
	}

}
