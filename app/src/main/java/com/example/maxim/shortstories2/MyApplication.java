package com.example.maxim.shortstories2;


import android.app.Application;
import android.content.Context;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.APIs.VkClient;
import com.example.maxim.shortstories2.walls.Wall;
import com.example.maxim.shortstories2.walls.WallTwitter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maxim.shortstories2.APIs.VkStrings.BASE_URL;

public class MyApplication extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Ullo2GMzTDF8bWnl2o0ocgXEE";
    private static final String TWITTER_SECRET = "NJyAKDkmg3VaQE0SDrRpaOlYckF6nGVFVSeDkJmbi50DTvAVdc";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();

    private static MyApplication instance;
    private static String accessToken;
    public static List<Wall> walls;

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final VkClient vkClient = retrofit.create(VkClient.class);

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
        TwitterSession twitterSession =
                Twitter.getInstance().core.getSessionManager().getActiveSession();
        if (twitterSession == null) {
            twitterApiClient = new MyTwitterApiClient();
        } else {
            twitterApiClient = new MyTwitterApiClient(twitterSession);
        }

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
