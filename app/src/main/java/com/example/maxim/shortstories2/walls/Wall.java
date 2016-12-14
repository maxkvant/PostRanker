package com.example.maxim.shortstories2.walls;
import com.example.maxim.shortstories2.post.Post;

import java.util.List;

public interface Wall {
    List<Post> getPosts(int offset, WALL_MODE mode);
    void update();
}