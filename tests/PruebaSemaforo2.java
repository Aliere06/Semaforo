public class PruebaSemaforo2 {
    public static void main (String[] args) {
        Object ObjetoDeControl = new Object();
        Semaforo2 s1 = new Semaforo2(ObjetoDeControl, "Semaforo 1");
        Semaforo2 s2 = new Semaforo2(ObjetoDeControl, "Semaforo 2");
        Semaforo2 s3 = new Semaforo2(ObjetoDeControl, "Semaforo 3");

        s1.start();
        s2.start();
        s3.start();
    }
}