import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PruebasDibujo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    public static void init() {
        JFrame ventana = new JFrame("Dibujos");
        JPanel panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponents(g);
                g.setColor(Color.DARK_GRAY);
                g.fillOval(0, 0, 200, 200);
            }
            @Override
            public Dimension getPreferredSize() {
                return isPreferredSizeSet() ?
				super.getPreferredSize() : new Dimension(500, 500);
            }
        };
        ventana.setSize(500, 500);
        ventana.add(panel);
        ventana.pack();
        ventana.setVisible(true);
    }
}
