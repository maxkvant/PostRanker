package com.example.maxim.shortstories2.walls;

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

import okhttp3.HttpUrl;
import okhttp3.Request;

import static com.example.maxim.shortstories2.MyApplication.okHttpClient;
import static com.example.maxim.shortstories2.walls.VkStrings.*;
import static java.lang.StrictMath.max;

public class WallVk extends AbstractWall {
    public WallVk(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public List<Post> getPosts(int offset, WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode,
                " and " + Post.PostsEntry.COLUMN_NAME_WALL_ID + " = " + id + " ");
    }

    @Override
    public boolean update() {
        String responseStr;

        long beforeGet = new Date().getTime();

        try {
            String url = HttpUrl.parse(url_get)
                    .newBuilder()
                    .addQueryParameter(param_name_version, version_api)
                    .addQueryParameter(param_name_id, id + "")
                    .addQueryParameter(param_name_date, updated + "")
                    .addQueryParameter(param_name_access_token, MyApplication.getAccessToken())
                    .toString();
            Request request = new Request.Builder().url(url).build();
            responseStr = okHttpClient.newCall(request).execute().body().string();
            Thread.sleep(1000 / 3);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        List<Post> posts = parsePosts(responseStr);
        if (posts == null) {
            return false;
        }

        if (ratio == 0) {
            double ratingSum = 0;
            for (Post post : posts) {
                ratingSum += post.rating + 1.0;
            }
            ratio = posts.size() / max(ratingSum, 1.0);
        }
        Log.d("update", "ratio = " + String.valueOf(ratio));
        List<Post> posts2 = new ArrayList<>();
        for (Post post : posts) {
            posts2.add(new Post(
                    post.text,
                    id,
                    name,
                    post.date,
                    post.rating * ratio
            ));
        }

        updated = beforeGet;
        new DBHelper().insertWall(this);
        (new DBHelper()).insertPosts(posts2);
        return true;
    }

    private List<Post> parsePosts(String responseStr) {
        List<Post> posts = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(responseStr);
            JSONArray jsonArray = responseJsonObject.getJSONArray(json_response);
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(json_text)) {
                    String likes = jsonObject.getJSONObject(json_likes).get(json_count).toString();
                    String text = jsonObject.get(json_text).toString().replace("\\\n", System.getProperty("line.separator"));
                    int date = Integer.parseInt(jsonObject.get(json_date).toString());
                    double rating = Integer.parseInt(likes);
                    rating = rating * rating;
                    posts.add(new Post(
                            text,
                            id,
                            name,
                            date,
                            rating
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return posts;
    }
}
