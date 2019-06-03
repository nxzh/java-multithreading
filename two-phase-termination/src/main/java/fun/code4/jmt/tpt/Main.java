package fun.code4.jmt.tpt;

import java.util.UUID;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        AlarmMgr alarmMgr = AlarmMgr.getInstance();
        alarmMgr.init();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                int i = 10;
                String id = UUID.randomUUID().toString();
                while (--i > 0) {
                    try {
                        Thread.sleep(1000);
                        alarmMgr.sendAlarm(AlarmType.FAULT, id, Thread.currentThread().getName() + " is dying...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                alarmMgr.sendAlarm(AlarmType.RESUME, id, Thread.currentThread().getName() + " get alive...");
            }
        };
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; ++i) {
            threads[i] = new Thread(task);
            threads[i].setName("Worker thread + " + i);
            threads[i].start();
        }
        Thread.sleep(2000);
        alarmMgr.shutdown();
    }
}
