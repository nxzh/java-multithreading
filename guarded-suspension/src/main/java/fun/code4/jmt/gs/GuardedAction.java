package fun.code4.jmt.gs;

import java.util.concurrent.Callable;

public abstract class GuardedAction<V> implements Callable<V> {
    protected final Predicate guard;

    protected GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}
