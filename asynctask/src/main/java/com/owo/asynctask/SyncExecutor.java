package com.owo.asynctask;

import java.util.concurrent.Executor;


public class SyncExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
