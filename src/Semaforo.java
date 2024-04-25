//C. SEMÁFORO
/* Clase semáforo que se puede ejecutar en un proceso independiente, la clase tiene como
 * atributo el objeto controlador que se utiliza para sincronizar multiples instacias de
 * esta clase.*/

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;

public class Semaforo extends Thread { //Para poderse ejecutar en otro hilo se extiende la clase thread
    private ControladorSemaforos controladorCompartido; // Referencia al controlador compartido
    private JFrame ventana = new JFrame("Semáforo");

    //CONSTRUCTOR
    public Semaforo(ControladorSemaforos controladorCompartido, String nombre) {
        super(nombre);
        this.controladorCompartido = controladorCompartido;
        Container panel = ventana.getContentPane();
        panel.setLayout(new GridLayout(3, 0));
        panel.setBackground(Color.DARK_GRAY);
        panel.add(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(ventana.getContentPane().getBackground());
                g.setColor(Color.GREEN);
                g.fillOval((int)(getWidth()*.05), (int)(getHeight()*.05), (int)(getWidth()*.9), (int)(getHeight()*.9));
            }
        });
        panel.add(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(ventana.getContentPane().getBackground());
                g.setColor(Color.YELLOW);
                g.fillOval((int)(getWidth()*.05), (int)(getHeight()*.05), (int)(getWidth()*.9), (int)(getHeight()*.9));
            }
        });
        panel.add(new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(ventana.getContentPane().getBackground());
                g.setColor(Color.RED);
                g.fillOval((int)(getWidth()*.05), (int)(getHeight()*.05), (int)(getWidth()*.9), (int)(getHeight()*.9));
            }
        });
        ventana.setSize(200, 600);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    //M. RUN
    /* Método run de la clase Thread que se ejecuta por defecto cuando se crea el hilo.
     * Se sobreescribe con la funcionalidad que deseamos que tenga nuestra clase.*/
    @Override
    public void run() {
        try { //Bloque "try-catch" para manejar excepciones de interrupción del hilo.
            while (true) { //Ciclo constante para verificar si el controlador está disponible.

                /* Bloque "if" que solo se ejecuta si el hilo es capaz de reclamar la
                 * disponibilidad del controlador.*/
                if (controladorCompartido.solicitarDisponible()) {
                    System.out.println("Verde");
                    sleep(2000); // Dormir por 2 segundos
                    System.out.println("Amarillo");
                    sleep(3000);
                    System.out.println("Rojo");
                    //sleep(500); // Dormir por .5 segundos
                    System.err.println(getName() + " Finalizado.");

                    /* Llamada para liberar el controlador al finalizar la ejecución de 
                     * la funcionalidad de la clase.*/
                    controladorCompartido.liberar();
                    break; // Salir del ciclo
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /* try {
            controladorCompartido.obtener();
            System.out.println("Verde");
            Thread.sleep(2000); // a dormir por 2 segundos
            System.out.println("Amarillo");
            Thread.sleep(2000);
            System.out.println("Rojo");
            controladorCompartido.devolver();

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } */
    }
}