package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.util.AsyncCall;
import com.example.maxim.shortstories2.util.Callback;

import java.util.List;

public abstract class AbstractFactoryWall implements FactoryWall {
    @Override
    public Wall toWall(SearchItem searchItem) {
        return create(searchItem.name, searchItem.id, 0, 0);
    }

    @Override
    public AsyncCall<List<SearchItem>> searchWalls(final String query, final Callback<List<SearchItem>> callback) {
        AsyncCall<List<SearchItem>> call = new AsyncCall<List<SearchItem>>() {
            @Override
            protected Callback.Result<List<SearchItem>> doInBackground(Void... voids) {
                try {
                    return Callback.Result.onSuccess(searchWalls(query));
                } catch (Exception e) {
                    return Callback.Result.onFailure(e);
                }
            }

            @Override
            protected void onPostExecute(Callback.Result<List<SearchItem>> result) {
                if (result.exception != null) {
                    callback.onFailure(result.exception);
                } else {
                    callback.onSuccess(result.result);
                }
            }
        };
        call.execute();
        return call;
    }
}
