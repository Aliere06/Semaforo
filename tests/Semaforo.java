public class Semaforo extends Thread {
    private Object ObjetoDeControl; // Referencia a objeto compartido

    public Semaforo(Object ObjetoDeControl, String nombre) {
        super(nombre);
        this.ObjetoDeControl = ObjetoDeControl;
    }

    public void run() {
        try {
            //Opción alternativa de sincronización
            /* En vez de utilizar una clase separada como controlador, 
             * hacemos uso de las herramientas incluidas en el lenguaje
             * para sincronizar los hilos.
             * Utilizamos un bloque "syncronized" que recibe un objeto
             * de referencia, este objeto no necesita tener ninguna 
             * funcionalidad específica ya que el lenguaje gestiona de 
             * forma interna que el bloque solo se pueda ejecutar por 
             * uno de los hilos que comparten el objeto de referencia 
             * a la vez.*/
            synchronized (ObjetoDeControl) {
                System.out.println("Verde");
                sleep(2000); // Dormir por 2 segundos
                System.out.println("Amarillo");
                sleep(3000);
                System.out.println("Rojo");
                System.err.println(getName() + " Finalizado.");
                //ObjetoDeControl.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}