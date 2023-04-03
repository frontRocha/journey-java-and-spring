public class Main {
    public static void main(String[] args) throws InterruptedException {
        var armazena = new Armazena();

        Thread t = new Thread(new ExecutandoTarefas(armazena));
        Thread t2 = new Thread(new ExecutandoTarefas(armazena));

        t.start();
        t2.start();

        t.join();
        t2.join();

        System.out.println(armazena.i);
    }
}
