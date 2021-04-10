package logika;

public class Koordinati {
	
	protected int y;
	protected int x;
	
	public Koordinati(int x, int y) throws Exception {
		preveri_koordinate(x, y);
		this.x = x;
		this.y = y;
	}
	
	public void preveri_koordinate(int x, int y) throws Exception {
		if (x < 0 || y < 0) throw new Exception("Prosimo, vnesite veljavni koordinati.");
	}
}
