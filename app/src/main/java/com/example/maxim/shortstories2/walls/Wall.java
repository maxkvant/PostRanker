package com.example.maxim.shortstories2.walls;
import com.example.maxim.shortstories2.post.Post;

import org.json.JSONException;

import java.util.List;

public interface Wall {
    List<Post> getPosts(int offset, int mode);
    void update();
}