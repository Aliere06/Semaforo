public class PruebaSemaforo {
    public static void main (String[] args) {
        ControladorSemaforos controladorCompartido = new ControladorSemaforos();
        Semaforo s1 = new Semaforo(controladorCompartido, "Semaforo 1");
        Semaforo s2 = new Semaforo(controladorCompartido, "Semaforo 2");
        Semaforo s3 = new Semaforo(controladorCompartido, "Semaforo 3");

        s1.start();
        s2.start();
        s3.start();
    }
}