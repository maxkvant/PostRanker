package com.example.maxim.shortstories2;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataHolder extends Application {
    private static DataHolder instance;

    public DataHolder() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static List<Wall> walls = new ArrayList<>();
    static {
        Log.d("DataHolder", "static");
        walls.addAll(Arrays.asList(new WallTop(), new WallAll()));
        walls.addAll(Arrays.asList(new WallVk("Подслушано"), new WallVk("Just Story")));
    }
}
