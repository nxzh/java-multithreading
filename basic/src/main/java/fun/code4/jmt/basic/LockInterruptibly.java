package fun.code4.jmt.basic;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptibly {
    public static final Lock lock = new ReentrantLock();
    public static final Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
//        testLock();
        testInterrupts();
    }

    private static void testLock() throws InterruptedException {
        Thread th = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("lock pass");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        lock.lock();
        th.start();
        Thread.sleep(1000);
        th.interrupt();
        Thread.sleep(1000);
        lock.unlock();
    }

    private static void testInterrupts() throws InterruptedException {
        Thread th = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println("lock pass");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        lock.lock();
        th.start();
        Thread.sleep(1000);
        th.interrupt();
        Thread.sleep(1000);
        lock.unlock();
    }
}
