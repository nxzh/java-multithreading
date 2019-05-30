package fun.code4.jmt.tpt.infra.tpt;

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {
    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    public AbstractTerminatableThread(TerminationToken terminationToken) {
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    protected abstract void doRun() throws Exception;

    protected void doCleanup(Exception cause) {

    }

    protected void doTerminate() {

    }

    @Override
    public void run() {
        System.out.println("+++ Alarm Agent task is running in thread:" + Thread.currentThread().getName());
        Exception ex = null;
        try {
            for (; ; ) {
                if (terminationToken.isToShutdown() && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            ex = e;
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    @Override
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminate();
        } finally {
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUntilThreadTerminated) {
        terminate();
        if (waitUntilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
