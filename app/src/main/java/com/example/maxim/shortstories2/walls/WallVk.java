package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.MyApplication;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.post.PostVK;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static com.example.maxim.shortstories2.MyApplication.okHttpClient;
import static com.example.maxim.shortstories2.walls.VkStrings.*;
import static java.lang.StrictMath.log;
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
        long startParsing = new Date().getTime();
        Gson gson = new Gson();
        List<Post> posts = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(responseStr);
            JSONArray jsonArray = responseJsonObject.getJSONArray(JSON_RESPONSE);

            PostVK[] postsVK = gson.fromJson(jsonArray.toString(), PostVK[].class);
            for (PostVK aPostsVK : postsVK) {
                String text = aPostsVK.text.replace("\\\n", System.getProperty("line.separator"));
                double rating = aPostsVK.likes.count;
                rating = rating * rating;
                posts.add(new Post(
                        text,
                        id,
                        name,
                        aPostsVK.date,
                        rating
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Log.d("parsing:", (new Date().getTime() - startParsing) + "");
        return posts;
    }

    public static List<SearchItem> search(String query) {
        String url = HttpUrl.parse(URL_SEARCH)
                .newBuilder()
                .addQueryParameter(PARAM_NAME_VERSION, VERSION_API)
                .addQueryParameter(PARAM_NAME_QUERY, query)
                .addQueryParameter(PARAM_NAME_LIMIT, 20 + "")
                .addQueryParameter(PARAM_NAME_SEARCH_TYPE, "1")
                .addQueryParameter(PARAM_NAME_ACCESS_TOKEN, MyApplication.getAccessToken())
                .toString();

        Request request = new Request.Builder().url(url).build();
        try {
            String responseStr = okHttpClient.newCall(request).execute().body().string();
            Log.d("SearchTask response", responseStr);

            return parseSearch(responseStr);
        } catch (IOException e) {
            return null;
        }
    }

    private static List<SearchItem> parseSearch(String responseStr) {
        List<SearchItem> res = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONObject(responseStr).getJSONArray(JSON_RESPONSE);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject cur = jsonArray.getJSONObject(i);
                    long id;
                    String name;
                    String type = cur.getString(JSON_TYPE);
                    String description = "";
                    if (cur.has(JSON_DESCRIPTION)) {
                        description = cur.get(JSON_DESCRIPTION) + "";
                    }
                    cur = cur.getJSONObject(type);
                    if (type.equals(GROUP_ITEM_TYPE)) {
                        id = -cur.getInt(JSON_ID);
                        name = cur.getString(JSON_NAME);
                    } else {
                        id = cur.getInt(JSON_ID);
                        String firstName = cur.getString(JSON_FIRST_NAME);
                        String lastName = cur.getString(JSON_LAST_NAME);
                        name = firstName + " " + lastName;
                    }
                    res.add(new SearchItem(name, id, description));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
}
