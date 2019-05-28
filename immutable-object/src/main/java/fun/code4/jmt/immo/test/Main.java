package fun.code4.jmt.immo.test;

public class Main {

    private static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Helper.lock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                while (flag) {
                    Lock lock = new Lock();
                    System.out.println(lock);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t1.start();
        t2.start();
        Thread.sleep(5000);
        flag = false;
    }
}
