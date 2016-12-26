package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.post.Post;

import java.util.List;

abstract class AbstractWall implements Wall {
    protected final String name;
    protected final long id;

    AbstractWall(String name, long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public long getId() {
        return id;
    }
}
