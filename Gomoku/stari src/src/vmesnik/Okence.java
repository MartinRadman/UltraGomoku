package vmesnik;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Okence extends JPanel {
    public JTextField player_1;
    public JTextField player_2;
    public JSlider x;
    public JSlider y;
    public JCheckBox comp_1;
    public JCheckBox comp_2;
    public JButton okay;
    public JButton back;
    public JLabel text_player_1;
    public JLabel text_player_2;
    public JLabel jcomp11;
    public JLabel jcomp12;

    public Okence() {
        //construct components
        player_1 = new JTextField (5);
        player_2 = new JTextField (5);
        x = new JSlider (0, 25);
        x.setValue(15);
        y = new JSlider (0, 25);
        y.setValue(15);
        comp_1 = new JCheckBox ("Računalnik");
        comp_2 = new JCheckBox ("Računalnik");
        okay = new JButton ("Začni");
        back = new JButton ("Nazaj");
        text_player_1 = new JLabel ("Igralec 1:");
        text_player_2 = new JLabel ("Igralec 2:");
        jcomp11 = new JLabel ("Število Stolpcev");
        jcomp12 = new JLabel ("Število vrstic");

        //set components properties
        x.setOrientation (JSlider.HORIZONTAL);
        x.setMinorTickSpacing (1);
        x.setMajorTickSpacing (5);
        x.setPaintTicks (true);
        x.setPaintLabels (true);
        y.setOrientation (JSlider.HORIZONTAL);
        y.setMinorTickSpacing (1);
        y.setMajorTickSpacing (5);
        y.setPaintTicks (true);
        y.setPaintLabels (true);
        

        //adjust size and set layout
        setPreferredSize (new Dimension (718, 385));
        setLayout (null);

        //add components
        add (player_1);
        add (player_2);
        add (x);
        add (y);
        add (comp_1);
        add (comp_2);
        add (okay);
        add (back);
        add (text_player_1);
        add (text_player_2);
        add (jcomp11);
        add (jcomp12);

        //set component bounds (only needed by Absolute Positioning)
        player_1.setBounds (95, 80, 100, 25);
        player_2.setBounds (95, 115, 100, 25);
        x.setBounds (120, 160, 565, 45);
        y.setBounds (120, 210, 565, 50);
        comp_1.setBounds (205, 75, 100, 25);
        comp_2.setBounds (205, 110, 100, 25);
        okay.setBounds (205, 345, 100, 25);
        back.setBounds (325, 345, 100, 25);
        text_player_1.setBounds (35, 80, 60, 25);
        text_player_2.setBounds (35, 115, 60, 20);
        jcomp11.setBounds (10, 165, 100, 25);
        jcomp12.setBounds (10, 215, 100, 25);
    }
}


