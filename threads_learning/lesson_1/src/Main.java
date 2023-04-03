public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + i);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + i);

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread2.start();
    }
}
