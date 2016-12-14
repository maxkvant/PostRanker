package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;

public class WallAll implements Wall {
    private final String name;

    public WallAll(String name, long id) {
        Log.d("WallAll", "construct: " + name + " " + id);
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
    @Override
    public List<Post> getPosts(int offset, WALL_MODE mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode, "");
    }

    @Override
    public void update() {
        for (Wall wall : walls) {
            if (wall != this) {
                wall.update();
            }
        }
    }
}
