package vmesnik;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import logika.Igra;

@SuppressWarnings("serial")
public class Platno_z_GridLayout extends JPanel {
	
	protected Color barva_igralca_1 = Color.BLACK;
	protected Color barva_igralca_2 = Color.WHITE;
	protected Color barva_ozadja = Color.LIGHT_GRAY;
	protected Igra igra;
	protected GridLayout mreza;

	
	public Platno_z_GridLayout(int sirina, int visina, Igra igra) {
		int polje_x = igra.dimenzija_polja_x() + 1;
		int polje_y = igra.dimenzija_polja_y();
		
        setLayout(new GridLayout(polje_x, polje_y));
        setBackground(barva_ozadja);
        
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        this.setMinimumSize(new Dimension(10, 200));
        
        for (int row = 0; row < polje_x; row++) {
            if (row > 0) {
                add(new JLabel(Character.toString((char) (('A' + row) - 1)), SwingConstants.CENTER));
            } else {
                add(new JLabel(" "));
            }
            for (int col = 0; col < polje_y; col++) {
                if (row == 0) {
                    add(new JLabel(Integer.toString(col + 1), SwingConstants.CENTER));
                } else {
                	JButton b = new JButton(" ");
                	b.setBackground(barva_ozadja);
                	b.setForeground(barva_ozadja);
                	b.setBorder(new LineBorder(Color.BLACK));
                	b.setPreferredSize(new Dimension(40, 40));
                    add(b);
                }
            }
        }
    }
	    

}