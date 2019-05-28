package fun.code4.jmt.immo.test;

public class Helper {
    public static void lock() throws InterruptedException {
        synchronized (Lock.class) {
            System.out.println("Enter Locking...");
            Thread.sleep(3000);
        }
    }
}
