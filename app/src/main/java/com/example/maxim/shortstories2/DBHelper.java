package com.example.maxim.shortstories2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "ShortStoriesDB";
    final static String TABLE_POSTS = "Posts";


    public DBHelper() {
        super(DataHolder.getContext(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate");
        String sql = "create table " + TABLE_POSTS + "(" +
                "id integer primary key," +
                "text text," +
                "wall text," +
                "date integer," +
                "rating integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertPost(Post post) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", post.text.hashCode());
        values.put("text", post.text);
        values.put("wall", post.wall);
        values.put("date", post.date);
        values.put("rating", post.rating);

        db.insert(TABLE_POSTS, null, values);
        db.close();
    }

    public List<Post> getPosts(String selectQuery) {
        List<Post> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();
                post.text = cursor.getString(cursor.getColumnIndex("text"));
                post.wall = cursor.getString(cursor.getColumnIndex("wall"));
                post.date = cursor.getInt(cursor.getColumnIndex("date"));
                post.rating = cursor.getInt(cursor.getColumnIndex("rating"));
                res.add(post);
            } while (cursor.moveToNext());
        }
        db.close();
        return res;
    }

    public void execSQL(String query) {
        Log.d("DBHelper", query);
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }
}
