package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class WallTop implements Wall {
    @Override
    public String toString() {
        return "Лучшие за день";
    }
    @Override
    public List<Post> getPosts() {
        DBHelper dbHelper = new DBHelper();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,-1);
        long oneDayBefore = (cal.getTimeInMillis() / 1000);
        String sql = "select * from " + DBHelper.TABLE_POSTS +
                " where date > " + String.valueOf(oneDayBefore) + ";" +
                " order by rating desc;";
        return dbHelper.getPosts(sql);
    }

    @Override
    public void deletePosts() {}

    @Override
    public void update() {}
}
