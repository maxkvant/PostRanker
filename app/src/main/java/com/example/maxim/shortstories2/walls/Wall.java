package com.example.maxim.shortstories2.walls;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.util.AsyncCall;
import com.example.maxim.shortstories2.util.Callback;

import java.util.List;

public interface Wall {
    long getId();
    double getRatio();
    long getUpdated();
    Cursor getPosts(WallMode mode) throws Exception;
    AsyncCall<Cursor> getPosts(WallMode mode, Callback<Cursor> callback);
    void update() throws Exception;
    AsyncCall<Void> update(Callback<Void> callback);
    String getFactoryClassName();
    boolean isSource();

    final class WallsEntry implements BaseColumns {
        final public static String TABLE_NAME = "Walls";
        final public static String COLUMN_NAME_ID = "id";
        final public static String COLUMN_NAME_NAME = "name";
        final public static String COLUMN_NAME_CLASS = "class";
        final public static String COLUMN_NAME_PRIORITY = "priority";
        final public static String COLUMN_NAME_UPDATED = "updated";
        final public static String COLUMN_NAME_RATIO = "ratio";
    }

}