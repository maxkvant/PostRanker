package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import java.util.List;

public class WallLatest implements Wall {
    @Override
    public String toString() {
        return "Последние";
    }
    @Override
    public List<Post> getPosts() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts("select * from " + DBHelper.TABLE_POSTS + " " +
                " order by date desc");
    }

    @Override
    public void deletePosts() {}

    @Override
    public void update() {}
}