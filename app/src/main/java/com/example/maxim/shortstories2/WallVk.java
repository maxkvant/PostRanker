package com.example.maxim.shortstories2;

import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class WallVk implements Wall {
    private String name;

    private List<Post> posts = new ArrayList<>();

    @Override
    public String toString() {
        return this.name;
    }

    public WallVk(String name) {
        this.name = name;
        String curResponse = "";
        switch (name) {
            case "Подслушано":
                getResponse("-34215577");
                break;
            case "Убойные Истории":
                getResponse("-55544604");
            case "Just Story":
                getResponse("-106084026");
                break;
            case "New Story":
                getResponse("-127509226");
        }
        try {
            parseJsonResponse(new JSONObject(curResponse));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponse(String id) {
        final String queryUrl = "https://api.vk.com/method/wall.get?count=11&filter=owner&extended=1"
                + "&owner_id=" + id;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(queryUrl,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                            parseJsonResponse(jsonObject);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        // Log error message
                        // to help solve any problems
                        Log.e("error http query", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    private void parseJsonResponse(JSONObject jsonObject) {
        ArrayList<Post> res = new ArrayList<>();
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
                        post.date = Long.parseLong(curJsonObject.get("date").toString());
                        post.wall = wallName;
                        String likes = curJsonObject.getJSONObject("likes").get("count").toString();

                        System.out.println(likes);
                        post.rating = Integer.parseInt(likes);
                        res.add(post);
                    }

                }
                Log.d("tag", String.valueOf(jsonArray.length()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        posts = res;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o.toString().equals(this.name);
    }
}
