package com.example.maxim.shortstories2.walls;
import android.provider.BaseColumns;

import com.example.maxim.shortstories2.post.Post;

import java.util.List;

public interface Wall {
    long getId();
    double getRatio();
    long getUpdated();
    List<Post> getPosts(int offset, WallMode mode);
    boolean update();
    String getFactoryClassName();

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