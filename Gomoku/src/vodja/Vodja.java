package vodja;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import inteligenca.Inteligenca;
import inteligenca.Minimax;
import logika.Igra;
import logika.Igra.Igralec;
import splosno.KdoIgra;
import splosno.Koordinati;
import vmesnik.Okno;

public class Vodja {	
	
	protected Map<Igralec,VrstaIgralca> vrstaIgralca;
	protected Map<Igralec,KdoIgra> kdoIgra;
	
	protected Okno okno;
	
	protected Igra igra = null;
	
	protected boolean clovekNaVrsti = false;
	
	public Vodja(Okno okno, Map<Igralec, VrstaIgralca> vrstaIgralca, Map<Igralec, KdoIgra> kdoIgra) {
		this.okno = okno;
		this.vrstaIgralca = vrstaIgralca;
		this.kdoIgra = kdoIgra;
	}
		
	public void igramoNovoIgro(int x, int y, String igralec1_ime, String igralec2_ime) {
		igra = new Igra(x, y, igralec1_ime, igralec2_ime);
	}
	
	public void igramo() {
		okno.osvezi_vmesnik();
		switch (igra.stanje()) {
		case ZMAGA_O: 
		case ZMAGA_X: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.na_potezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				clovekNaVrsti = false;
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}

	
	public Inteligenca racunalnikovaInteligenca = new Minimax(2);
	
	public void igrajRacunalnikovoPotezo() { // preveri pravilnost	
		Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
		igra.odigraj(poteza);
		okno.odigraj(poteza); //treba narediti, da platno črpa podatke iz igre
		igramo();
		
		/*
		Igra zacetkaIgra = igra;
		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				System.out.println("doInBackground " + poteza);
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null; 
				try {poteza = get();} catch (Exception e) {};	
				System.out.println("done " + poteza);
				if (igra == zacetkaIgra) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
		*/
		
	
	}
		
	public void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		okno.osvezi_vmesnik();
		igramo();
	}
	
	public Igra igra() {
		return this.igra;
	}


}
