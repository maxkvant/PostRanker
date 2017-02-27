package com.example.maxim.shortstories2.walls;

import android.os.AsyncTask;

import com.example.maxim.shortstories2.util.AsyncCall;
import com.example.maxim.shortstories2.util.Callback;

import java.io.Serializable;
import java.util.List;

public interface FactoryWall extends Serializable {
    Wall create(String name, long id, double ratio, long updated);
    List<SearchItem> searchWalls(String query) throws Exception;
    AsyncCall searchWalls(String query, Callback<List<SearchItem>> callback);
    Wall toWall(SearchItem searchItem);
}

