/* C. CONTROLADOR DE SEMAFOROS
 * Clase controladora que actúa como objeto compartido para syncronizar 
 * diferentes procesos, todos los procesos que se deseen sincronizar en 
 * conjunto deben contener una referencia a una misma instancia de esta 
 * clase.*/
public class ControladorSemaforos {
    //Atributo booleano para indicar la disponibilidad del controlador
    private boolean disponible = true;

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