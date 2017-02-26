package com.example.maxim.shortstories2;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.APIs.VkClient;
import com.example.maxim.shortstories2.util.SharedPrefs;
import com.example.maxim.shortstories2.walls.FactoryWall;
import com.example.maxim.shortstories2.walls.FactoryWallAll;
import com.example.maxim.shortstories2.walls.Wall;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maxim.shortstories2.APIs.VkStrings.BASE_URL;
import static com.example.maxim.shortstories2.util.Strings.FALSE;
import static com.example.maxim.shortstories2.util.Strings.FIRST_RUN;
import static com.example.maxim.shortstories2.util.Strings.VK_ACCESS_TOKEN;
import static com.example.maxim.shortstories2.walls.WallMode.TOP_ALL;

public class MyApplication extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Ullo2GMzTDF8bWnl2o0ocgXEE";
    private static final String TWITTER_SECRET = "NJyAKDkmg3VaQE0SDrRpaOlYckF6nGVFVSeDkJmbi50DTvAVdc";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();

    private static MyApplication instance;
    private static String accessToken;
    private static Context context;

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final VkClient vkClient = retrofit.create(VkClient.class);

    public static volatile MyTwitterApiClient twitterApiClient;

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
        SharedPrefs.storeString(context, VK_ACCESS_TOKEN, accessToken);
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

        context = getApplicationContext();
        accessToken = SharedPrefs.getString(context, VK_ACCESS_TOKEN);

        Log.d("MyApplication", "onCreate first_run:");
        if (SharedPrefs.getString(this, FIRST_RUN) == null) {
            Wall wallAll = new FactoryWallAll().create();
            Log.d("MyApplication", "onCreate FirstRun");
            new MainActivity.Helper(wallAll, TOP_ALL)
                    .refresh(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("MyApplication", "onCreate Refreshed");
                            SharedPrefs.storeString(MyApplication.this, FIRST_RUN, FALSE);
                        }
                    });
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
