package com.example.maxim.shortstories2.walls;

import android.text.Html;
import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.MyApplication;
import com.example.maxim.shortstories2.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static com.example.maxim.shortstories2.MyApplication.okHttpClient;

public class WallVk implements Wall {
    private final String name;
    private final long id;

    public WallVk(String name, long id) {
        Log.d("WallVk", "construct: " + name + " " + id);
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public List<Post> getPosts(int offset, WALL_MODE mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode,
                " and wall_id = " + id + " ");
    }

    @Override
    public void update() {
        String responseStr = "";
        try {
            String url = HttpUrl.parse("https://api.vk.com/method/execute.getPostsMonthly")
                    .newBuilder()
                    .addQueryParameter("v", "5.60")
                    .addQueryParameter("id", String.valueOf(id))
                    .addQueryParameter("access_token", MyApplication.getAccessToken())
                    .toString();
            Request request = new Request.Builder().url(url).build();
            responseStr = okHttpClient.newCall(request).execute().body().string();
            Thread.sleep(1000 / 3);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        List<Post> posts = parsePosts(responseStr);
        (new DBHelper()).insertPosts(posts);
    }

    private List<Post> parsePosts(String responseStr) {
        List<Post> posts = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(responseStr);
            JSONArray jsonArray = responseJsonObject.getJSONArray("response");
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("text")) {
                    String likes = jsonObject.getJSONObject("likes").get("count").toString();
                    String text = (Html.fromHtml(jsonObject.get("text").toString())).toString();
                    int date = Integer.parseInt(jsonObject.get("date").toString());
                    posts.add(new Post(
                            text,
                            id,
                            name,
                            date,
                            Integer.parseInt(likes)
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
