/* C. CONTROLADOR DE SEMAFOROS
 * Clase controladora que actúa como objeto compartido para syncronizar 
 * diferentes procesos, todos los procesos que se deseen sincronizar en 
 * conjunto deben contener una referencia a una misma instancia de esta 
 * clase.*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControladorSemaforos {
    //Atributo booleano para indicar la disponibilidad del controlador
    private boolean disponible = true;
    JFrame ventana = new JFrame("Controlador de Semáforos");
    TablaInformación tabla = new TablaInformación();
    int semáforosAgregados = 0;

    public class TablaInformación extends JPanel{
        String[][] celdas = new String[4][6];
        //String[] fila = new String[4];
        String[] cabecera = {"Semáforo", "Estado", "Foco Activo", "Terminar"};
        JPanel[] columnas = new JPanel[4];
        int filasOcupadas;
        
        TablaInformación(){
            setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));
            setBackground(Color.DARK_GRAY);
            for (int i=0; i<columnas.length; i++) {
                columnas[i] = new JPanel(new GridLayout(6,1,0,0));
                columnas[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                columnas[i].add(new JLabel(cabecera[i]));
                add(columnas[i]);
            }
        }

        public void agregarFila(String[] fila) {
            if (filasOcupadas < celdas[0].length) {
                for (int i = 0; i < (columnas.length - 1); i++) {
                    columnas[i].add(new JLabel(fila[i]));
                }
                JPanel xPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                xPane.add(new JLabel("X"));
                columnas[3].add(xPane);
                filasOcupadas++;
                getParent().revalidate();
                getParent().repaint();
            }
        }

        public void eliminarFila(int indice) {
            if (indice > 0 && indice <= filasOcupadas) {
                for (int i = 0; i < columnas.length; i++) {
                    columnas[i].remove(indice);
                }
                filasOcupadas--;
                getParent().revalidate();
                getParent().repaint();
            }
        }

        public void actualizarFila(int indice, String[] fila) {
            if (indice <= filasOcupadas) {
                Component[] nombres = columnas[0].getComponents();
                for (int i = 1; i < nombres.length; i++) {
                    JLabel etiqueta = (JLabel)nombres[i];
                    if (etiqueta.getText().equals(fila[0])) {
                        ((JLabel)columnas[1].getComponent(i)).setText(fila[1]);
                        ((JLabel)columnas[2].getComponent(i)).setText(fila[2]);
                        break;
                    }
                }
                revalidate();
                repaint();
            }
        }
    }

    ControladorSemaforos(){
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(350, 300);
        Container contenedor = ventana.getContentPane();
        contenedor.setLayout(new BorderLayout(0,0));
        contenedor.setBackground(Color.DARK_GRAY);
        
        JPanel titulo = new JPanel(new BorderLayout(0,0));
        titulo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));
        titulo.setBackground(Color.GRAY);
        JLabel lblTitulo = new JLabel("Controlador de semáforos");
        JButton btnAgregar = new JButton("Agregar");
        titulo.add(lblTitulo, BorderLayout.WEST);
        titulo.add(btnAgregar, BorderLayout.EAST);


        /* JPanel tabla = new JPanel(new FlowLayout());
        tabla.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel columna1 = new JPanel(new GridLayout(6, 1));
        columna1.add(new JLabel("Semáforo"));
        tabla.add(columna1);
        JPanel columna2 = new JPanel(new GridLayout(6, 1));
        columna2.add(new JLabel("Estado"));
        tabla.add(columna2);
        JPanel columna3 = new JPanel(new GridLayout(6, 1));
        columna3.add(new JLabel("Foco Activo"));
        tabla.add(columna3);
        JPanel columna4 = new JPanel(new GridLayout(6, 1));
        columna4.add(new JLabel("Terminar"));
        tabla.add(columna4); */

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(tabla, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    //M. ESTÁ DISPONIBLE
    /* Método sincronizado que retorna el estado de disponibilidad del 
     * controlador. A pesar de que la lectura del atributo no tiene 
     * efectos secundarios, el método se tiene que indicar como sincronizado 
     * para que no se ejecute al mismo tiempo que los demás métodos de la
     * clase.*/
    public synchronized boolean estáDisponible() {
        return disponible;
    }

    //M. LIBERAR
    //Método sincronizado que marca el controlador como disponible.
    public synchronized void liberar() {
        disponible = true;
        notify();
    }

    //M. SOLICITAR DISPONIBLE
    /* Método sincronizado que retorna verdadero y bloquea el controlador 
     * si este se encuentra disponible, retorna falso si no lo está. 
     * Este método se utiliza para convertir el proceso de verificar y
     * reclamar la disponibilidad del controlador en una operación atómica.*/
    public synchronized boolean solicitarDisponible() {
        if (this.estáDisponible()) {
            disponible = false;
            return true;
        } else {
            return false;
        }
    }


    //M. DESCONTINUADOS
    /* public synchronized void devolver() {
        while(disponible)
        {   try{	wait();
        }catch(InterruptedException ex){}
        }

        disponible=true;
        notify();
    } */

    /* public synchronized void obtener() {
        while(!disponible)
        {	try{	wait();
        }catch(InterruptedException ex){}
        }
        disponible=false;
        notify();
    } */
}