package com.example.maxim.shortstories2;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maxim.shortstories2.walls.Wall;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static List<Wall> walls;
    private static String accessToken;
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken != null) {
                accessToken = newToken.toString();
            } else {
                accessToken = null;
            }
        }
    };

    public static void setAccessToken(String accessToken) {
        MyApplication.accessToken = accessToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public MyApplication() {
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static String getAccessToken() {
        return accessToken;
    }

}
