package fun.code4.jmt.tpt;

import fun.code4.jmt.tpt.infra.tpt.AbstractTerminatableThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AlarmSendingThread extends AbstractTerminatableThread {
    private final AlarmAgent alarmAgent = new AlarmAgent();

    private final BlockingQueue<AlarmInfo> alarmQueue;
    private final ConcurrentMap<String, AtomicInteger> submittedAlarmRegistry;

    public AlarmSendingThread() {
        alarmQueue = new ArrayBlockingQueue<AlarmInfo>(100);
        submittedAlarmRegistry = new ConcurrentHashMap<String, AtomicInteger>();
        alarmAgent.init();
    }


    @Override
    protected void doRun() throws Exception {
        AlarmInfo alarm;
        alarm = alarmQueue.take();
        terminationToken.reservations.decrementAndGet();
        try {
            alarmAgent.sendAlarm(alarm);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 只要收到一个 RESUME 警告, 就将所有的 Registry 中对应所有的相同 ID 的 FAULT 警告移除.
        if (AlarmType.RESUME == alarm.getType()) {
            String key = AlarmType.FAULT.toString() + ':' + alarm.getId() + '@' + alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);
            key = AlarmType.RESUME.toString() + ':' + alarm.getId() + '@' + alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);
        }
    }

    public int sendAlarm(final AlarmInfo alarmInfo) {
        AlarmType type = alarmInfo.getType();
        String id = alarmInfo.getId();
        String extraInfo = alarmInfo.getExtraInfo();
        if (terminationToken.isToShutdown()) {
            System.out.println("rejected alarm:" + id + "," + extraInfo);
            return -1;
        }

        int duplicateSubmissionCount = 0;
        try {
            AtomicInteger prevSubmittedCounter;

            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(type.toString() + ':' + id + '@' + extraInfo, new AtomicInteger(0));
            if (null == prevSubmittedCounter) {
                terminationToken.reservations.incrementAndGet();
                alarmQueue.put(alarmInfo);
            } else {
                duplicateSubmissionCount = prevSubmittedCounter.incrementAndGet();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return duplicateSubmissionCount;
    }

    protected void doCleanup(Exception exp) {
        if (null != exp && !(exp instanceof InterruptedException)) {
            exp.printStackTrace();
        }
        alarmAgent.disconnect();
    }
}
