package com.example.maxim.shortstories2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WallTop implements Wall {
    @Override
    public String toString() {
        return "Top";
    }
    @Override
    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        for (Wall wall : DataHolder.walls) {
            if (wall instanceof WallVk) {
                posts.addAll(wall.getPosts());
            }
        }
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o1.rating - o2.rating;
            }
        });
        Collections.reverse(posts);
        return posts;
    }
}
