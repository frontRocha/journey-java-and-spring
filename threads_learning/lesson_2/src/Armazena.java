public class Armazena {
    public long i;

    public  void somar() {
        System.out.println("mensagem");
        synchronized (this) {
            this.i++;
        }
    }
}
