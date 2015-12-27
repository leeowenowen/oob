package com.owo.asynctask;

import java.util.ArrayList;
import java.util.List;

import com.owo.asynctask.TaskReducer.TaskContext;

public class AllTask<Param, Result>
        extends
        CompositeTask<AllTask<Param, Result>, Void, List<Result>, Param, Result> {
    public AllTask() {
        mTaskReducer.withClient(new ReducerClient());
    }

    private class ReducerClient implements TaskReducer.Client<Param, Result> {
        @Override
        public void onOneTaskDone(TaskReducer<Param, Result> taskReducer,
                TaskReducer.TaskContext<Param, Result> taskContext) {
        }

        @Override
        public void onAllTaskDone(TaskReducer<Param, Result> taskReducer,
                List<TaskReducer.TaskContext<Param, Result>> taskContexts) {
            List<Result> results = new ArrayList<Result>();
            for (TaskContext<Param, Result> taskContext : taskContexts) {
                results.add(taskContext.getResult());
            }
            mCallback.run(results);
        }
    }
}
