public class PruebaSemaforo {
    public static void main (String[] args) {
        Object ObjetoDeControl = new Object();
        Semaforo s1 = new Semaforo(ObjetoDeControl, "Semaforo 1");
        Semaforo s2 = new Semaforo(ObjetoDeControl, "Semaforo 2");
        Semaforo s3 = new Semaforo(ObjetoDeControl, "Semaforo 3");

        s1.start();
        s2.start();
        s3.start();
    }
}