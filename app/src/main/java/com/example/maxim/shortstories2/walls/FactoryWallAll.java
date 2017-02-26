package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import java.util.List;

public class FactoryWallAll extends AbstractFactoryWall {

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new WallAll(name, id, ratio, updated);
    }

    public Wall create() {
        return create("", 0, 0, 0);
    }

    @Override
    public List<SearchItem> searchWalls(String query) {
        return null;
    }
}

class WallAll extends AbstractWall {
    public WallAll(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public List<Post> getPosts(int offset, WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode);
    }

    @Override
    public boolean update() {
        List<Wall> walls = new DBHelper().getAllWalls();
        for (Wall wall : walls) {
            if (wall.isSource()) {
                if (!wall.update()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isSource() {
        return false;
    }

    @Override
    public String getFactoryClassName() {
        return FactoryWallAll.class.getSimpleName();
    }
}
