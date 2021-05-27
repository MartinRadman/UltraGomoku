package inteligenca;

import logika.Igra;
import splosno.Koordinati;

public class Inteligencakrneki extends splosno.KdoIgra {

	public Inteligencakrneki(String ime) {
		super(ime);
	}
	
	public Inteligencakrneki() {
		this("Martin Radman in Gašper Petrovič");
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		Koordinati najboljsaPoteza = monte_carlo(igra);
		return najboljsaPoteza;	
	}
	
	public Koordinati monte_carlo(Igra igra) {
		Drevo koren = new Drevo(igra);
		MonteCarloTreeSearch iskalec = new MonteCarloTreeSearch(koren, igra.na_potezi(), igra);
		iskalec.napolni_s_podlisti(koren, igra);
		Drevo najboljsi = iskalec.monte_carlo_tree_search();
		return najboljsi.k;
		
	}
}

