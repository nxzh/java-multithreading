package fun.code4.jmt.tpt;



import fun.code4.jmt.tpt.infra.gs.Blocker;
import fun.code4.jmt.tpt.infra.gs.ConditionVarBlocker;
import fun.code4.jmt.tpt.infra.gs.GuardedAction;
import fun.code4.jmt.tpt.infra.gs.Predicate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class AlarmAgent {
    /**
     * 是否连接到报警服务器
     */
    private volatile boolean connectedToServer = false;

    private final Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            return connectedToServer;
        }
    };

    /**
     * 守卫, 用于在连接到服务器之前, 阻塞其他线程发送消息
     */
    private final Blocker blocker = new ConditionVarBlocker();

    /**
     * Daemon线程, 用于心跳检测
     */
    private final Timer heartbeatTimer = new Timer(true);

    /**
     * 发送报警消息, 守卫会先检查前提条件(predicate)是否满足:
     * 若满足, 则执行 doSendAlarm 发送实际消息
     * 若不满足, 则阻塞调用线程(caller)
     * @param alarm
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };
        blocker.callWithGuard(guardedAction);
    }

    /**
     * 发送实际的报警消息, 调用此方法的前提使, 报警服务器已经调用成功
     * @param alarm
     */
    private void doSendAlarm(AlarmInfo alarm) {
        System.out.println("Sending alarm  " + alarm + " in thread " + Thread.currentThread().getName());
        // 模拟发送到服务器的耗时
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {

        }
    }

    /**
     * 初始化:
     * 1. 开启服务器连接线程
     * 2. 60 秒后开启一个 2 秒间隔的心跳线程 (Daemon)
     */
    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();
        heartbeatTimer.schedule(new HeartbeatTask(), 60000, 2000);
    }

    public void disconnect() {
        connectedToServer = false;
    }

    /**
     * 连接成功后的 hook
     */
    protected void onConnected() {
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connectedToServer = true;
                    return Boolean.TRUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDisconnected() {
        connectedToServer = false;
    }

    private class ConnectingTask implements Runnable {


        @Override
        public void run() {
            // 省略其他代码
            System.out.println("+++ Connecting Task is running in thread:" + Thread.currentThread().getName());
            // 模拟连接耗时
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            onConnected();
        }
    }

    private class HeartbeatTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("+++ Heartbeat Task is running in thread:" + Thread.currentThread().getName());
            if (!testConnection()) {
                onDisconnected();
                reconnect();
            }
        }
    }

    private boolean testConnection() {
        return true;
    }

    private void reconnect() {
        ConnectingTask connectingThread = new ConnectingTask();
        connectingThread.run();
    }
}
