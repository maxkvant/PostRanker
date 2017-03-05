package com.example.maxim.shortstories2.util;

import android.os.AsyncTask;
import android.telecom.Call;

public class AsyncCall<T> extends AsyncTask<Void, Void, Callback.Result<T>> {
    private final CallableException<T> callable;
    private final Callback<T> callback;

    public AsyncCall(CallableException<T> callable, Callback<T> callback) {
        this.callable = callable;
        this.callback = callback;
    }

    @Override
    final protected Callback.Result<T> doInBackground(Void... voids) {
        try {
            return Callback.Result.onSuccess(callable.call());
        } catch (Exception e) {
            return Callback.Result.onFailure(e);
        }
    }

    @Override
    final protected void onPostExecute(Callback.Result<T> result) {
        if (result.exception != null) {
            callback.onFailure(result.exception);
        } else {
            callback.onSuccess(result.result);
        }
    }

}
