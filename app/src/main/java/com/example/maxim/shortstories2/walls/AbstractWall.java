package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.post.Post;

import java.util.List;

abstract class AbstractWall implements Wall {
    protected final String name;
    protected final long id;
    protected double ratio;

    AbstractWall(String name, long id, double ratio) {
        this.name = name;
        this.id = id;
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getRatio() {
        return ratio;
    }
}
