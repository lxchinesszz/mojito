package cn.lxchinesszz.mojito.net.future;

import cn.lxchinesszz.mojito.net.future.listener.MojitoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxin
 * 2022/8/9 20:50
 */
public class MojitoFuture<V> implements Promise<V>, Future<V> {

    /**
     * volatile 多线程保证可见性
     */
    private volatile V result;

    /**
     * AtomicReferenceFieldUpdater 多线程保证操作的原子性
     */
    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<MojitoFuture, Object> RESULT_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(MojitoFuture.class, Object.class, "result");

    /**
     * 当前承诺的监听器
     */
    private final List<MojitoListener<V>> listeners = new ArrayList<>();

    /**
     * 默认空值,如果失败或者撤销,使用EMPTY
     */
    private final static Object EMPTY = new Object();

    /**
     * 是否被撤销了
     */
    private boolean cancelled = false;

    /**
     * 并发锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * get 阻塞条件,用于完成时候唤醒get阻塞线程
     */
    private final Condition condition = lock.newCondition();

    /**
     * get超时阻塞条件,,用于完成时候唤醒get阻塞线程
     */
    private final Condition timeoutCondition = lock.newCondition();


    /**
     * 原子操作
     *
     * @param objResult 数据
     * @return boolean
     */
    private boolean setValue0(Object objResult) {
        if (RESULT_UPDATER.compareAndSet(this, null, objResult) ||
                RESULT_UPDATER.compareAndSet(this, EMPTY, objResult)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSuccess() {
        return isDone() && result != EMPTY;
    }

    @Override
    public void setSuccess(V data) {
        boolean updateSuccess;
        try {
            lock.lock();
            updateSuccess = setValue0(data);
            condition.signal();
            timeoutCondition.signal();
        } finally {
            lock.unlock();
        }
        if (updateSuccess) {
            for (MojitoListener<V> listener : listeners) {
                listener.onSuccess(data);
            }
        }
    }

    @Override
    public void setFailure(Throwable cause) {
        boolean updateSuccess;
        try {
            lock.lock();
            updateSuccess = setValue0(EMPTY);
            condition.signalAll();
            timeoutCondition.signalAll();
        } finally {
            lock.unlock();
        }
        if (updateSuccess) {
            for (MojitoListener<V> listener : listeners) {
                listener.onThrowable(cause);
            }
        }
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.cancelled = mayInterruptIfRunning;
        return false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        // 只要不等于空,说明就是有结果了,不管成功或者失败
        return result != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        lock.lock();
        try {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            } else {
                // 如果没有完成并且没有撤销
                if (!isDone() && !isCancelled()) {
                    // 释放线程,任务在这里阻塞,等待完成时候释放.
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
        return isSuccess() ? result : null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        lock.lock();
        try {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            } else {
                // 如果没有完成并且没有撤销
                if (!isDone() && !isCancelled()) {
                    // 释放线程,任务在这里阻塞,等待完成时候释放.
                    boolean await = timeoutCondition.await(timeout, unit);
                    if (!await) {
                        throw new TimeoutException();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        return isSuccess() ? result : null;
    }

    @Override
    public void addListeners(MojitoListener<V> listener) {
        this.listeners.add(listener);
    }
}
