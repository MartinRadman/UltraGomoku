package inteligenca;


import java.util.List;
import java.util.Random;

import logika.Igra;

import splosno.Koordinati;


import inteligenca.NajboljseOcenjenePoteze;

public class RandomMinimax extends Inteligenca {
	
	private static final Random RANDOM = new Random();
	
	private static final int ZMAGA = 100; // vrednost zmage, več kot vsaka druga ocena pozicije
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	private int globina;
	
	public RandomMinimax (int globina) {
		super("naključni minimax (" + globina + ")");
		this.globina = globina;		
	}
	
	@Override
	public Koordinati izberiPotezo (Igra igra) {
		List<OcenjenaPoteza> ocenjenePoteze = najboljsePoteze(igra, this.globina);
		System.out.println(ocenjenePoteze.size() + " potez z vrednostjo " + ocenjenePoteze.get(0).ocena);
		int i = RANDOM.nextInt(ocenjenePoteze.size());	
		return ocenjenePoteze.get(i).poteza;		
	}
	
	// vrne seznam vseh potez, ki imajo največjo vrednost z vidike trenutnega igralca na potezi
	public static List<OcenjenaPoteza> najboljsePoteze(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.poteze();
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra); 
			kopijaIgre.odigraj (p);	//poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O:
			case ZMAGA_X: ocena = ZMAGA; break; // p je zmagovalna poteza
			case NEODLOCENO: ocena = NEODLOC; break;
			default: //nekdo je na potezi
				if (globina==1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre,igra.naPotezi());
				else ocena = //negacija ocene z vidike dgrugega igralca
						-najboljsePoteze(kopijaIgre,globina-1).get(0).ocena;  
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(p, ocena));			
		}
		return najboljsePoteze.list();
	}

	
}
