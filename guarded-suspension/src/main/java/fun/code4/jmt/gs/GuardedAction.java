package fun.code4.jmt.gs;

import java.util.concurrent.Callable;

/**
 * 带有前提条件的 Callable.
 * 只有当 Predicate 满足的时候, 才会执行 Callable 的 call 方法
 * @param <V>
 */
public abstract class GuardedAction<V> implements Callable<V> {
    protected final Predicate guard;

    protected GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}
