package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.util.AsyncCall;
import com.example.maxim.shortstories2.util.CallableException;
import com.example.maxim.shortstories2.util.Callback;

import java.util.List;

public abstract class AbstractWallFactory implements WallFactory {
    @Override
    public Wall toWall(SearchItem searchItem) {
        return create(searchItem.name, searchItem.id, 0, 0);
    }

    @Override
    public AsyncCall<List<SearchItem>> searchWalls(final String query, final Callback<List<SearchItem>> callback) {
        AsyncCall<List<SearchItem>> call = new AsyncCall<>(
                new CallableException<List<SearchItem>>() {
                    @Override
                    public List<SearchItem> call() throws Exception {
                        return searchWalls(query);
                    }
                },
                callback);
        call.execute();
        return call;
    }
}
