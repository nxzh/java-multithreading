package fun.code4.jmt.basic;

import java.util.Date;

public class WaitTimeoutAndNotify {
    static Object o1 = new Object();

    public static void main(String[] args) {
        ThreadB b = new ThreadB();
        b.start();
        System.out.println("b is start....");
        synchronized (o1)// 主线程获取o1的对象锁
        {
            try {
                System.out
                        .println(new Date() + "Waiting for b to complete...");
                o1.wait(3000);//o1的对象锁释放2s，主线程进入等待状态,需要通知才能获取锁
                System.out
                        .println(new Date() + "Completed.Now back to main thread");
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Total is :" + b.total);
    }

    static class ThreadB extends Thread {
        int total;

        public void run() {
            synchronized (o1) {//ThreadB获取o1的对象锁
                System.out.println("ThreadB is running..");
                for (int i = 0; i < 5; i++) {
                    total += i;
                    System.out.println("total is " + total);
                }
                try {
//               Thread.sleep(5000);//ThreadB执行完后，主线程继续执行
                    Thread.sleep(2000);//ThreadB先执行完，然后主线程再等待1s,主线程继续执行
//               Thread.sleep(3000);//ThreadB先执行完，紧接着主线程继续执行
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //o1.notify();//ThreadB释放o1的对象锁，通知其他等待o1对象锁的线程继续运行
            }


        }
    }
}

