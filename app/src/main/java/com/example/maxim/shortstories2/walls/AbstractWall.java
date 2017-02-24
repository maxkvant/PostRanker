package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.post.Post;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.max;

public abstract class AbstractWall implements Wall {
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

    protected List<Post> withRatio(List<Post> posts) {
        if (ratio == 0) {
            double ratingSum = 0;
            for (Post post : posts) {
                ratingSum += post.rating + 1.0;
            }
            ratio = posts.size() / max(ratingSum, 1.0);
        }
        Log.d("update", "ratio = " + String.valueOf(ratio));

        List<Post> posts2 = new ArrayList<>();
        for (Post post : posts) {
            posts2.add(new Post(
                    post.text,
                    id,
                    name,
                    post.date,
                    post.rating * ratio
            ));
        }
        return posts2;
    }
}
