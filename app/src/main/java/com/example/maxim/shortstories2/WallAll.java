package com.example.maxim.shortstories2;

import java.util.List;

public class WallAll implements Wall {
    @Override
    public String toString() {
        return "Всё подряд";
    }
    @Override
    public List<Post> getPosts() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts("select * from " + DBHelper.TABLE_POSTS + " " +
                " order by date desc");
    }

    @Override
    public void deletePosts() {

    }
}