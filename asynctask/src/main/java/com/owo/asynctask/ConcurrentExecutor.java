package com.owo.asynctask;

import java.util.concurrent.Executor;

public class ConcurrentExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        TaskRunner.run(command);
    }
}
