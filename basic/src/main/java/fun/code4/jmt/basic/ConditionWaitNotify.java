package fun.code4.jmt.basic;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionWaitNotify {
    private Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ConditionWaitNotify service = new ConditionWaitNotify();
        new Thread(service::await).start();
        Thread.sleep(1000 * 3);
        service.signal();
        Thread.sleep(1000);
    }

    public void await() {
        try {
            lock.lock();
            System.out.println("await的时间为 " + System.currentTimeMillis());
            condition.await();
            System.out.println("await结束的时间" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("sign的时间为" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
