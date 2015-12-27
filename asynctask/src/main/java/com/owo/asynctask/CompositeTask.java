package com.owo.asynctask;

public class CompositeTask<Impl, Param, Result, ReducerParam, ReducerResult>
        implements Task<Param, Result> {
    protected TaskReducer<ReducerParam, ReducerResult> mTaskReducer = new TaskReducer<>();
    protected Param mParam;
    protected Callback<Result> mCallback;

    @SuppressWarnings("unchecked")
    public Impl of(Task<ReducerParam, ReducerResult> task, ReducerParam param) {
        mTaskReducer.of(task, param);
        return (Impl) this;
    }

    @Override
    public void run(Param param, Callback<Result> callback) {
        mParam = param;
        mCallback = callback;
        mTaskReducer.start();
    }

}
