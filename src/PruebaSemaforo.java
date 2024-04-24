//Clase para probar la sincronización
public class PruebaSemaforo {
    public static void main (String[] args) {
        //Objeto controlador compartido
        ControladorSemaforos controladorCompartido = new ControladorSemaforos();

        //Semáforos que utilizan el mismo controlador
        Semaforo s1 = new Semaforo(controladorCompartido, "Semaforo 1");
        Semaforo s2 = new Semaforo(controladorCompartido, "Semaforo 2");
        Semaforo s3 = new Semaforo(controladorCompartido, "Semaforo 3");

        //Ejecución de los semáforos
        //Nota: se llama el método "start" y no "run" para que la ejecución sea concurrente
        s1.start();
        s2.start();
        s3.start();
    }
}