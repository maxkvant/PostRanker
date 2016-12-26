package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.post.Post;

import java.util.List;

abstract class AbstractWall implements Wall {
    protected final String name;
    protected final long id;
    protected double ratio;
    protected long updated;

    AbstractWall(String name, long id, double ratio, long updated) {
        this.name = name;
        this.id = id;
        this.ratio = ratio;
        this.updated = updated;
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

    @Override
    public long getUpdated() {
        return updated;
    }
}
