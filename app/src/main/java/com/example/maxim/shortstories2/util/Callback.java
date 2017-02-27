package com.example.maxim.shortstories2.util;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);

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
}
