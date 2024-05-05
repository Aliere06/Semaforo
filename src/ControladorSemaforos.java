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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    int semaforosAgregados = 0;
    ArrayList<Semaforo> semaforos = new ArrayList<>();

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
                JLabel etiquetaEncabezado = new JLabel(cabecera[i]);
                etiquetaEncabezado.setBackground(Color.BLACK);
                etiquetaEncabezado.setForeground(Color.WHITE);
                columnas[i].add(etiquetaEncabezado);
                add(columnas[i]);
            }

        }

        public void agregarFila(String[] fila) {
            if (filasOcupadas < celdas[0].length) {
                for (int i = 0; i < (columnas.length - 1); i++) {
                    columnas[i].add(new JLabel(fila[i]));
                }
                JPanel xPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                JLabel xLabel = new JLabel("X");

                xLabel.setForeground(Color.RED); //le declare el color rojo para distinguirlo del resto de la tabla
                xLabel.addMouseListener(new MouseAdapter() { //EL LISTINER DE CLIC
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Semaforo semaforo = buscarSemaforo(3, xPane);
                        int noFila = semaforo.indiceTabla;
                        semaforo.detener();
                        eliminarFila(noFila);
                    }
                }); //FIN DEL LISTENER


                xPane.add(xLabel);
                columnas[3].add(xPane);
                filasOcupadas++;
                getParent().revalidate();
                getParent().repaint();
            }
        }
        public synchronized void eliminarSemaforo(int id) {
            // Elimina el semáforo con el ID proporcionado de la tabla
            tabla.eliminarFila(id);
            // También puedes agregar más lógica para detener o finalizar el hilo del semáforo si es necesario
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

        public Semaforo buscarSemaforo(int noColumna, Component elementoAsociado) {
            JPanel columna = columnas[noColumna];
            List<Component> elementos = Arrays.asList(columna.getComponents());

            JLabel etiquetaSemaforo;
            int noFila;
            try {
                noFila = elementos.indexOf(elementoAsociado);
                etiquetaSemaforo = (JLabel)columnas[0].getComponent(noFila);
            } catch (Exception e) {
                System.out.println("El elemento indicado no existe en la columna o es nulo");
                e.printStackTrace();
                return null;
            }
            String nombreSemaforo = etiquetaSemaforo.getText();
            System.out.println("Semáforo en fila: " + nombreSemaforo);

            System.out.println("Buscando semaforos...");
            for (Semaforo semaforo : semaforos) {
                System.out.println(semaforo.getName());
                if (semaforo.getName().equals(nombreSemaforo)) {
                    semaforo.indiceTabla = noFila;
                    return semaforo;
                }
            }
            return null;
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

        // Agregamos ActionListener al botón "btnAgregar"
        btnAgregar.addActionListener(new ActionListener() { //LISTENER DE AGREGAR
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear un nuevo semáforo y agregarlo al controlador compartido
                Semaforo nuevoSemaforo = new Semaforo(ControladorSemaforos.this);
                nuevoSemaforo.start();
            }
        });

        titulo.add(lblTitulo, BorderLayout.WEST);
        titulo.add(btnAgregar, BorderLayout.EAST);

        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(tabla, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    //M. GET
    public int getSemaforosAgregados() {
        return semaforosAgregados;
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

    public void agregarSemaforo(Semaforo semaforo) {
        semaforos.add(semaforo);
        semaforosAgregados++;
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