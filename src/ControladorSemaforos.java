public class ControladorSemaforos {
    private boolean disponible = true;

    public synchronized boolean estáDisponible() {
        return disponible;
    }

    public synchronized void liberar() {
        disponible = true;
    }

    public synchronized boolean solicitarDisponible() {
        if (this.estáDisponible()) {
            disponible = false;
            return true;
        } else {
            return false;
        }
    }



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