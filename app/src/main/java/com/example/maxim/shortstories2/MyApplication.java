package com.example.maxim.shortstories2;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maxim.shortstories2.walls.MyTwitterApiClient;
import com.example.maxim.shortstories2.walls.Wall;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Ullo2GMzTDF8bWnl2o0ocgXEE";
    private static final String TWITTER_SECRET = "NJyAKDkmg3VaQE0SDrRpaOlYckF6nGVFVSeDkJmbi50DTvAVdc";

    private static MyApplication instance;
    public static List<Wall> walls;
    private static String accessToken;
    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();

    public static MyTwitterApiClient twitterApiClient;

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
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        twitterApiClient = new MyTwitterApiClient();

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
