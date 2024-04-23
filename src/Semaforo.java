public class Semaforo extends Thread {
    private ControladorSemaforos controladorCompartido; // Referencia a objeto compartido

    public Semaforo(ControladorSemaforos controladorCompartido, String nombre) {
        super(nombre);
        this.controladorCompartido = controladorCompartido;
    }

    public void run() {
        try {
            while (true) {
                if (controladorCompartido.solicitarDisponible()) {
                    System.out.println("Verde");
                    sleep(2000); // Dormir por 2 segundos
                    System.out.println("Amarillo");
                    sleep(3000);
                    System.out.println("Rojo");
                    //sleep(500); // Dormir por .5 segundos
                    System.err.println(getName() + " Finalizado.");
                    controladorCompartido.liberar();
                    break;
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