package com.example.maxim.shortstories2.post;

public class Comment {
    public final long post_id;
    public final String text;
    public final long id;

    public Comment(long post_id, String text, long id) {
        this.post_id = post_id;
        this.text = text;
        this.id = id;
    }
}
