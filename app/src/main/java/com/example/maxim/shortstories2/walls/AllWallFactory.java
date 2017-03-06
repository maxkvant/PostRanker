package com.example.maxim.shortstories2.walls;

import android.database.Cursor;

import com.example.maxim.shortstories2.DBHelper;

import java.util.List;

public class AllWallFactory extends AbstractWallFactory {

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new AllWall(name, id, ratio, updated);
    }

    public Wall create() {
        return create("", 0, 0, 0);
    }

    @Override
    public List<SearchItem> searchWalls(String query) throws Exception {
        throw new UnsupportedOperationException();
    }
}

class AllWall extends AbstractWall {
    public AllWall(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public Cursor getPosts(WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(mode);
    }

    @Override
    public void update() throws Exception {
        List<Wall> walls = new DBHelper().getAllWalls();
        for (Wall wall : walls) {
            if (wall.isSource()) {
                wall.update();
            }
        }
    }

    @Override
    public boolean isSource() {
        return false;
    }

    @Override
    public String getFactoryClassName() {
        return AllWallFactory.class.getSimpleName();
    }
}
