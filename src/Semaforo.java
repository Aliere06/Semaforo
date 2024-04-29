//C. SEMÁFORO
/* Clase semáforo que se puede ejecutar en un proceso independiente, la clase tiene como
 * atributo el objeto controlador que se utiliza para sincronizar multiples instacias de
 * esta clase.*/
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Map;

public class Semaforo extends Thread { //Para poderse ejecutar en otro hilo se extiende la clase thread
    public static final Map <Color, String> COLORES = Map.of(Color.GREEN, "Verde", Color.YELLOW, "Amarillo", Color.RED, "Rojo");
    private ControladorSemaforos controladorCompartido; // Referencia al controlador compartido
    private JFrame ventana = new JFrame("Semáforo");
    private Foco[] focos = new Foco[3];
    private String nombre;
    private boolean activo = false;
    private Foco focoActivo;
    private int id;
    int indiceTabla;

    public class Foco extends JPanel {
        Color colorEncendido, colorApagado, colorActivo;
        String nombreColor;

        Foco(Color color, Color fondo){
            setBackground(fondo);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            colorEncendido = color;
            colorApagado = colorEncendido.darker().darker().darker();
            colorActivo = colorApagado;
            nombreColor = Semaforo.COLORES.get(color);
        }

        public void encenderExclusivo() {
            for (Foco foco : focos) {
                if (foco != this) {
                    foco.colorActivo = foco.colorApagado;
                } else {
                    foco.colorActivo = foco.colorEncendido;
                    focoActivo = foco;
                    nombreColor = Semaforo.COLORES.get(colorActivo);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //g.setColor(Color.BLACK);
            //g.drawRect(0, 0, getWidth(), getHeight());
            g.setColor(colorActivo);
            g.fillOval((int)(getWidth()*.05), (int)(getHeight()*.05), (int)(getWidth()*.9), (int)(getHeight()*.9));
        }
    }

    //CONSTRUCTOR
    public Semaforo(ControladorSemaforos controladorCompartido) {
        this.controladorCompartido = controladorCompartido;
        controladorCompartido.agregarSemaforo(this);
        id = controladorCompartido.getSemaforosAgregados();
        nombre = "Semáforo " + id;
        ventana = new JFrame("S" + id);
        super.setName(nombre);
        
        Container panel = ventana.getContentPane();
        panel.setLayout(new GridLayout(3, 0));
        panel.setBackground(Color.DARK_GRAY);
        panel.add(focos[0] = new Foco(Color.GREEN, panel.getBackground()));
        panel.add(focos[1] = new Foco(Color.YELLOW, panel.getBackground()));
        panel.add(focos[2] = new Foco(Color.RED, panel.getBackground()));
        ventana.setSize(200, 600);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setContentPane(panel);
        ventana.setVisible(true);
        
        focos[2].encenderExclusivo();
        controladorCompartido.tabla.agregarFila(getInformación());
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
                    activo = true;
                    focos[0].encenderExclusivo();
                    controladorCompartido.tabla.actualizarFila(id, getInformación());
                    ventana.repaint();
                    System.out.println("Verde");
                    sleep(2000); // Dormir por 2 segundos
                    focos[1].encenderExclusivo();
                    controladorCompartido.tabla.actualizarFila(id, getInformación());
                    ventana.repaint();
                    System.out.println("Amarillo");
                    sleep(3000);
                    focos[2].encenderExclusivo();
                    activo = false;
                    controladorCompartido.tabla.actualizarFila(id, getInformación());
                    ventana.repaint();
                    System.out.println("Rojo");
                    System.err.println(getName() + " Finalizado.");
                    
                    /* Llamada para liberar el controlador al finalizar la ejecución de 
                    * la funcionalidad de la clase.*/
                    controladorCompartido.liberar();
                    sleep(500); // Dormir por .5 segundos
                    //break; // Salir del ciclo
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void detener() {
        controladorCompartido.liberar();
        ventana.dispose();
        interrupt();
    }

    public String[] getInformación() {
        String[] info = new String[3];
        info[0] = nombre;
        info[1] = activo ? "Activo" : "Esperando";
        info[2] = focoActivo.nombreColor;
        return info;
    }
}