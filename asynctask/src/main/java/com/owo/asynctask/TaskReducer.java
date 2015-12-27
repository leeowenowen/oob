package com.owo.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskReducer<Param, Result> {
    public static interface TaskContext<Param, Result> {
        Task<Param, Result> getTask();

        Param getParam();

        Result getResult();
    }

    private static class TaskContextImpl<Param, Result> implements
            TaskContext<Param, Result> {
        private final Task<Param, Result> mTask;
        private final Param mParam;
        private Result mResult;

        public TaskContextImpl(Task<Param, Result> task, Param param) {
            mTask = task;
            mParam = param;
        }

        @Override
        public Task<Param, Result> getTask() {
            return mTask;
        }

        @Override
        public Param getParam() {
            return mParam;
        }

        @Override
        public Result getResult() {
            return mResult;
        }

        public void setResult(Result result) {
            mResult = result;
        }
    }

    public static interface Client<Param, Result> {
        void onOneTaskDone(TaskReducer<Param, Result> taskReducer,
                TaskContext<Param, Result> taskContext);

        void onAllTaskDone(TaskReducer<Param, Result> taskReducer,
                List<TaskContext<Param, Result>> taskContexts);
    }

    protected Client<Param, Result> mClient;

    public TaskReducer<Param, Result> withClient(Client<Param, Result> client) {
        mClient = client;
        return this;
    }

    private Executor mExecutor;

    public TaskReducer<Param, Result> executor(Executor executor) {
        mExecutor = executor;
        return this;
    }

    private List<TaskContext<Param, Result>> mTaskContexts = new ArrayList<>();

    public TaskReducer<Param, Result> of(Task<Param, Result> task, Param param) {
        mTaskContexts.add(new TaskContextImpl<Param, Result>(task, param));
        return this;
    }

    public void start() {
        if (mTaskContexts.size() == 0) {
            return;
        }
        if (mExecutor == null) {
            mExecutor = new ConcurrentExecutor();
        }

        for (TaskContext<Param, Result> taskContext : mTaskContexts) {
            final TaskContextImpl<Param, Result> taskContextImpl = (TaskContextImpl<Param, Result>) taskContext;
            Callback<Result> callback = null;
            if (mClient != null) {
                callback = new Callback<Result>() {
                    @Override
                    public void run(Result result) {
                        taskContextImpl.setResult(result);
                        mTaskCompleteCallback.run(taskContextImpl);
                    }
                };
            }
            taskContext.getTask().run(taskContext.getParam(), callback);
        }
    }

    private int mFinishCount;
    private TaskCompleteCallback mTaskCompleteCallback = new TaskCompleteCallback();

    private class TaskCompleteCallback implements
            Callback<TaskContextImpl<Param, Result>> {
        @Override
        synchronized public void run(
                TaskContextImpl<Param, Result> taskContextImpl) {
            mFinishCount++;
            if (mFinishCount == 1 && mTaskContexts.size() == 1) {
                mClient.onOneTaskDone(TaskReducer.this, taskContextImpl);
                mClient.onAllTaskDone(TaskReducer.this, mTaskContexts);
            }
        }
    }
}
