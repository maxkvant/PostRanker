package com.example.maxim.shortstories2.util;

public interface CallableException<T> {
    T call() throws Exception;
}
