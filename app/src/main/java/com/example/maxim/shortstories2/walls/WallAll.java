package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import java.util.List;
import java.util.StringTokenizer;

import static android.content.Context.WALLPAPER_SERVICE;
import static com.example.maxim.shortstories2.MyApplication.walls;

public class WallAll extends AbstractWall {
    public WallAll(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public List<Post> getPosts(int offset, WALL_MODE mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode, "");
    }

    @Override
    public boolean update() {
        for (Wall wall : walls) {
            if (wall != this) {
                if (!wall.update()) {
                    return false;
                }
            }
        }
        return true;
    }
}
