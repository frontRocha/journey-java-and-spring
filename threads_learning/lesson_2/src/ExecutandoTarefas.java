public class ExecutandoTarefas implements Runnable {
    private Armazena armazena;

    public ExecutandoTarefas(Armazena armazena) {
        this.armazena = armazena;
    }

    @Override
    public void run() {
        for(int i = 0; i < 50000; i++) {
            this.armazena.somar();
        }
    }
}
