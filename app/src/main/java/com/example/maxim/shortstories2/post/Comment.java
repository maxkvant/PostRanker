package com.example.maxim.shortstories2.post;

import android.provider.BaseColumns;

public class Comment {
    public final long post_id;
    public final String text;
    public final long id;

    public Comment(long post_id, String text, long id) {
        this.post_id = post_id;
        this.text = text;
        this.id = id;
    }

    public final static class CommentsEntry implements BaseColumns {
        final public static String TABLE_NAME = "Comments";
        final public static String COLUMN_NAME_ID = "id";
        final public static String COLUMN_NAME_TEXT = "text";
        final public static String COLUMN_NAME_POST_ID = "post_id";
    }
}
