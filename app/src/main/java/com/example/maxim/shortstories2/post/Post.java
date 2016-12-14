package com.example.maxim.shortstories2.post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Post {
    public final String text;
    public final long wall_id;
    public final String wall_name;
    public final int date;
    public final int load_date;
    public final int rating;
    public final int id;

    public Post(String text, long wall_id, String wall_name, int date, int rating) {
        this.text = text;
        this.wall_id = wall_id;
        this.wall_name = wall_name;
        this.date = date;
        this.rating = rating;
        this.load_date = (int)(new Date().getTime() / 1000);
        this.id = text.hashCode();
    }
}
