package com.example.maxim.shortstories2.util;

import android.os.AsyncTask;

public class AsyncCall<T> extends AsyncTask<Void, Void, Result<T>> {
    private final CallableException<T> callable;
    private final Callback<T> callback;

    public AsyncCall(CallableException<T> callable, Callback<T> callback) {
        this.callable = callable;
        this.callback = callback;
    }

    @Override
    final protected Result<T> doInBackground(Void... voids) {
        try {
            return Result.onSuccess(callable.call());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.onFailure(e);
        }
    }

    @Override
    final protected void onPostExecute(Result<T> result) {
        if (result.exception != null) {
            callback.onFailure(result.exception);
        } else {
            callback.onSuccess(result.result);
        }
    }
}

class Result<T> {
    public final T result;
    public final Exception exception;

    private Result(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    public static <T> Result<T> onSuccess(T result) {
        return new Result<T>(result, null);
    }

    public static <T> Result<T> onFailure(Exception exception) {
        return new Result<T>(null, exception);
    }
}
