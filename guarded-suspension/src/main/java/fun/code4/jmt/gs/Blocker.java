package fun.code4.jmt.gs;

import java.util.concurrent.Callable;

/**
 * 用于控制 GuardedAction 的执行.
 */
public interface Blocker {

    /**
     * 控制 GuardedAction的执行:
     * 若条件不满足: 则阻塞
     * 若条件满足: 则执行
     * @param guardedAction
     * @param <V> 返回值的类型
     * @return 返回 GuaredAction 的结果
     * @throws Exception
     */
    <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception;

    /**
     * 在 signal 之前触发钩子
     * @param stateOperation 钩子
     * @throws Exception
     */
    void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    /**
     * 唤醒, 以便让 GuardedAction 执行
     * @throws InterruptedException
     */
    void signal() throws InterruptedException;

    /**
     * 在 signalAll 之前触发的钩子
     * @param stateOperation
     * @throws Exception
     */
    void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;
}
