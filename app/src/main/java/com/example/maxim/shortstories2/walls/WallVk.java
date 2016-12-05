package com.example.maxim.shortstories2.walls;

import android.text.Html;
import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WallVk implements Wall {
    private String name;
    private long id;

    @Override
    public String toString() {
        return this.name;
    }

    public WallVk(String name) {
        this.name = name;
        switch (name) {
            case "Подслушано":
                this.id = -34215577;
                break;
            case "Убойные Истории":
                this.id = -55544604;
            case "Just Story":
                this.id = -106084026;
                break;
            case "New Story":
                this.id = -127509226;
        }
        update();
    }

    @Override
    public void deletePosts() {
        DBHelper dbHelper = new DBHelper();
        dbHelper.execSQL("delete from " + DBHelper.TABLE_POSTS + " where wall = \'" + name + "\';");
    }

    @Override
    public void update() {
        getResponse();
    }

    private void getResponse() {
        final String queryUrl = "https://api.vk.com/method/wall.get?count=11&filter=owner&extended=1"
                + "&owner_id=" + String.valueOf(id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(queryUrl,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        onJsonResponse(jsonObject);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Log.e("error http query", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    public List<Post> getPosts() {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts("select * from " + dbHelper.TABLE_POSTS +
                " where wall = \'" + name + "\'" +
                "order by date desc;");
    }

    private void onJsonResponse(JSONObject jsonObject) {
        DBHelper dbHelper = new DBHelper();
        try {
            if(jsonObject.has("response")) {
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray jsonArray = response.getJSONArray("wall");

                String wallName = "";
                JSONArray groups = response.getJSONArray("groups");
                if (groups.length() > 0) {
                    wallName = groups.getJSONObject(0).get("name").toString();
                }

                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject curJsonObject = jsonArray.getJSONObject(i);
                    if (curJsonObject.has("text")) {
                        Post post = new Post();
                        post.text = (Html.fromHtml(curJsonObject.get("text").toString())).toString();
                        post.date = Integer.parseInt(curJsonObject.get("date").toString());
                        post.wall = wallName;
                        String likes = curJsonObject.getJSONObject("likes").get("count").toString();
                        post.rating = Integer.parseInt(likes);
                        dbHelper.insertPost(post);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
