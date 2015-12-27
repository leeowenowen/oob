package com.owo.asynctask;

import java.util.List;

import com.owo.asynctask.TaskReducer.Client;

public class AnyTask<Param, Result> extends
        CompositeTask<AllTask<Param, Result>, Void, Result, Param, Result> {
    public AnyTask() {
        mTaskReducer.withClient(new ReducerClient());
    }

    private class ReducerClient implements Client<Param, Result> {
        @Override
        public void onOneTaskDone(TaskReducer<Param, Result> taskReducer,
                TaskReducer.TaskContext<Param, Result> taskContext) {
            mCallback.run(taskContext.getResult());
        }

        @Override
        public void onAllTaskDone(TaskReducer<Param, Result> taskReducer,
                List<TaskReducer.TaskContext<Param, Result>> taskContexts) {
        }

    }

}
