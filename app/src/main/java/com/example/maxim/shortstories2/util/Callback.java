package com.example.maxim.shortstories2.util;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
