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

        long beforeGet = new Date().getTime() / 1000;

        try {
            String url = HttpUrl.parse(URL_GET)
                    .newBuilder()
                    .addQueryParameter(PARAM_NAME_VERSION, VERSION_API)
                    .addQueryParameter(PARAM_NAME_ID, id + "")
                    .addQueryParameter(PARAM_NAME_DATE, updated + "")
                    .addQueryParameter(PARAM_NAME_ACCESS_TOKEN, MyApplication.getAccessToken())
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

        posts = withRatio(posts);

        updated = beforeGet;
        new DBHelper().insertWall(this);
        (new DBHelper()).insertPosts(posts);
        return true;
    }

    private List<Post> parsePosts(String responseStr) {
        List<Post> posts = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(responseStr);
            JSONArray jsonArray = responseJsonObject.getJSONArray(JSON_RESPONSE);
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(JSON_TEXT)) {
                    String likes = jsonObject.getJSONObject(JSON_LIKES).get(JSON_COUNT).toString();
                    String text = jsonObject.get(JSON_TEXT).toString().replace("\\\n", System.getProperty("line.separator"));
                    int date = Integer.parseInt(jsonObject.get(JSON_DATE).toString());
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
