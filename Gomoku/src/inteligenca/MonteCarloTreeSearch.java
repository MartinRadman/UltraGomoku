package inteligenca;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igra.Igralec;
import logika.Igra.Stanje;
import splosno.Koordinati;

public class MonteCarloTreeSearch {
	
	final int casovna_omejitev = 4800; // v milisekundah
	final double c = Math.sqrt(2);
	protected long start = System.currentTimeMillis();
	protected long konec = start + casovna_omejitev;
	
	
	protected Drevo koren;
	protected Igralec jaz;
	protected Igra igra;
	
	public MonteCarloTreeSearch(Drevo koren, Igralec jaz, Igra igra) {
		this.koren = koren;
		this.jaz = jaz;
		this.igra = igra;
	}

	
	public Drevo monte_carlo_tree_search() {
	  while (System.currentTimeMillis() < konec) {
	        Drevo list = prehodi(koren); // list = neobiskan node
	        list.je_obiskan = true;
	        int rezultat_simulacije = odigraj_do_konca(list);
	        izpolni_za_nazaj(list, rezultat_simulacije);
	  }
	  return najboljsi_podlist(koren);
	}
	
	public Drevo prehodi(Drevo list) {
		Drevo nov_list = null;
	    while (popolnoma_obiskani_podlisti(list)) {
	        nov_list = najboljsi_uct(nov_list);
	    }
	    if (nov_list != null) return nov_list; 
	    return izberi_neobiskanega(list.sez_listov); // in case no children are present / node is terminal
	}
	
	public boolean popolnoma_obiskani_podlisti(Drevo list) {
		for (Drevo podlist : list.sez_listov) {
			if (podlist.je_obiskan == false) return false;
		}
		return true;
	}
	
	public double izracunaj_uct(Drevo list) {
		double vrednost_lista = (double) list.v;
		double obiskanost_lista = (double) list.n;
		double obiskanost_korena = (double) koren.n;
		
		double prvi_clen = vrednost_lista / obiskanost_lista;
		double v_korenu = Math.log(obiskanost_korena / obiskanost_lista);
		double drugi_clen = c * Math.sqrt(v_korenu);
		
		return prvi_clen + drugi_clen;
	}
	
	public Drevo najboljsi_uct(Drevo list) {
		Drevo najboljsi = null;
		double uct_najboljsi = 0;
		for (Drevo podlist : list.sez_listov) {
			double uct_nov = izracunaj_uct(podlist);
			if (najboljsi == null || uct_nov > uct_najboljsi) {
				najboljsi = podlist;
				uct_najboljsi = uct_nov;
			}
		}
		return najboljsi;
	}
	
	public Drevo izberi_neobiskanega(List<Drevo> sez_listov) {
		List<Drevo> neobiskani = new ArrayList<Drevo>();
		for (Drevo list : sez_listov) {
			if (!list.je_obiskan) neobiskani.add(list);
		}
		return izberi_nakljucno(neobiskani);
	}
	
	public Drevo izberi_nakljucno(List<Drevo> sez_listov) {
		Random rand = new Random();
		return sez_listov.get(rand.nextInt(sez_listov.size()));
	}
	    		
	 	
	public int odigraj_do_konca(Drevo list) {
		napolni_s_podlisti(list);
	    while (je_nekoncen(list)) {
	        list = pravilo_igranja(list);
	        napolni_s_podlisti(list);
	    }
	    return rezultat(list);
	}
	
	public void napolni_s_podlisti(Drevo list) {
		if (list.sez_listov.size() != 0) return;
		HashSet<Koordinati> moznePoteze = igra.mnozica_potez();
		List<Drevo> odigrane_poteze = new ArrayList<Drevo>();
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			Drevo poteza = new Drevo(igra, p, 0, 0, new ArrayList<Drevo>());
			odigrane_poteze.add(poteza);
		}
		list.sez_listov = odigrane_poteze;
		
	}
	
	public boolean je_nekoncen(Drevo list) {
		return (!(list.sez_listov.size() == 0));
	}
	
	public Drevo pravilo_igranja(Drevo list) {
		return izberi_nakljucno(list.sez_listov);
	}
	
	public int rezultat(Drevo list) {
		int rezultat = -2;
		Igra igra = list.igra;
		Stanje stanje = igra.stanje();
		switch(stanje) {
		case ZMAGA_O: rezultat = (jaz == Igralec.O) ? 1 : -1; break;
		case ZMAGA_X: rezultat = (jaz == Igralec.X) ? 1 : -1; break;
		case NEODLOCENO: rezultat = 0;
		case V_TEKU: new Error(); break;
		default: new Error(); break;
		}
		if (rezultat == -2) new Error();
		return rezultat;
	}


    public void izpolni_za_nazaj(Drevo list, int rezultat) {
    	if (list.je_koren()) return ;
    	list.v += rezultat;
	    izpolni_za_nazaj(list.s, rezultat);
    }

    
    public Drevo najboljsi_podlist(Drevo list) {
    	Drevo najboljsi = null;
    	int najboljsi_n = 0;
		for (Drevo podlist : list.sez_listov) {
			int nov_n = podlist.n;
			if (najboljsi == null || nov_n > najboljsi_n) {
				najboljsi = podlist;
				najboljsi_n = nov_n;
			}
		}
		return najboljsi;
    }
}
