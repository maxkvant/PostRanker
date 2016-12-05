package com.example.maxim.shortstories2;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maxim.shortstories2.walls.Wall;
import com.example.maxim.shortstories2.walls.WallLatest;
import com.example.maxim.shortstories2.walls.WallTop;
import com.example.maxim.shortstories2.walls.WallVk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {
    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static List<Wall> walls = new ArrayList<>();
    static {
        walls.addAll(Arrays.asList(new WallTop(), new WallLatest()));
        walls.addAll(Arrays.asList(new WallVk("Подслушано"), new WallVk("Just Story")));
    }
}
