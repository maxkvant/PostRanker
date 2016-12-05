package com.example.maxim.shortstories2;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WallTop implements Wall {
    @Override
    public String toString() {
        return "Лучшие";
    }
    @Override
    public List<Post> getPosts() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts("select * from " + DBHelper.TABLE_POSTS +
        " order by rating desc;");
    }
}
