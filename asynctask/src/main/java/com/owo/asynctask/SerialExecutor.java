package com.owo.asynctask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

public class SerialExecutor implements Executor {
    private final Queue<Runnable> mTasks = new LinkedList<Runnable>();
    private Runnable mActive;

    public synchronized void execute(final Runnable r) {
        mTasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (mActive == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((mActive = mTasks.poll()) != null) {
            TaskRunner.run(mActive);
        }
    }

    public synchronized void clear() {
        mTasks.clear();
    }
}
