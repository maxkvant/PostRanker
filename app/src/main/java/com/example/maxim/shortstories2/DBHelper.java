package com.example.maxim.shortstories2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maxim.shortstories2.post.Post;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "ShortStoriesDB";
    public final static String TABLE_POSTS = "Posts";


    public DBHelper() {
        super(MyApplication.getInstance(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

        db.insertWithOnConflict(TABLE_POSTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
        cursor.close();
        db.close();
        return res;
    }

    public void execSQL(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }
}
