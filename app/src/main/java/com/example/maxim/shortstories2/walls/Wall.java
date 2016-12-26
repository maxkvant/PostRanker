package com.example.maxim.shortstories2.walls;
import android.os.Bundle;

import com.example.maxim.shortstories2.post.Post;

import java.util.List;
import java.util.StringTokenizer;

public interface Wall {
    long getId();
    double getRatio();
    List<Post> getPosts(int offset, WALL_MODE mode);
    boolean update();
}