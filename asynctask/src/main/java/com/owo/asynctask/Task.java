package com.owo.asynctask;

public interface Task<Param, Result> {
    void run(Param param, Callback<Result> callback);
}
