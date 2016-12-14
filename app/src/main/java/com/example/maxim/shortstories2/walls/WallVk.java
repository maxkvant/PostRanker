package com.example.maxim.shortstories2.walls;

import android.text.Html;
import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

import static com.example.maxim.shortstories2.MyApplication.okHttpClient;

public class WallVk implements Wall {
    private final String name;
    private final long id;

    @Override
    public String toString() {
        return this.name;
    }

    public WallVk(String name, long id) {
        Log.d("WallVk", "construct: " + name + " " + id);
        this.name = name;
        this.id = id;
    }

    public List<Post> getPosts(int offset, WALL_MODE mode) {
        DBHelper dbHelper = new DBHelper();

        return dbHelper.getPosts(offset, mode,
                " and wall_id = " + id + " ");
    }

    @Override
    public void update() {
        final String queryUrl = "https://api.vk.com/method/wall.get?count=11&filter=owner&extended=1"
                + "&owner_id=" + String.valueOf(id);

        Log.d("WallVk update url", queryUrl);
        DBHelper dbHelper = new DBHelper();
        String responseStr = "";
        try {
            Request request = new Request.Builder().url(queryUrl).build();
            Log.d("WallVk", "before request");
            synchronized (okHttpClient) {
                responseStr = okHttpClient.newCall(request).execute().body().string();
            }
            Log.d("WallVk", "after request");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("WallVk update str", responseStr);
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            if(jsonObject.has("response")) {
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray jsonArray = response.getJSONArray("wall");

                String wallName = "";
                JSONArray groups = response.getJSONArray("groups");
                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject curJsonObject = jsonArray.getJSONObject(i);
                    if (curJsonObject.has("text")) {
                        String likes = curJsonObject.getJSONObject("likes").get("count").toString();
                        String text = (Html.fromHtml(curJsonObject.get("text").toString())).toString();
                        int date = Integer.parseInt(curJsonObject.get("date").toString());
                        Post post = new Post(
                            text,
                            id,
                            name,
                            date,
                            Integer.parseInt(likes)
                        );

                        dbHelper.insertPost(post);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
