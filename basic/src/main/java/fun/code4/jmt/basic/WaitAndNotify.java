package fun.code4.jmt.basic;

public class WaitAndNotify {
    static Object o1 = new Object();//可以是任意一个对象，或者自定义的对象

    public static void main(String[] args) {
        ThreadB b = new ThreadB();
        b.start();
        System.out.println("b is start....");
        synchronized (o1)// 主线程获取o1的对象锁
        {
            try {
                System.out
                        .println("Waiting for b to complete...");
                o1.wait();//o1的对象锁释放，主线程进入等待状态
                System.out
                        .println("Completed.Now back to main thread");
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
                o1.notify();//ThreadB释放o1的对象锁，通知其他等待o1对象锁的线程继续运行
            }
        }

    }
}
