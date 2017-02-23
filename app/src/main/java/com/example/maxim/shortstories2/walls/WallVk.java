package com.example.maxim.shortstories2.walls;

import android.util.Log;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.APIs.PostVk;
import com.example.maxim.shortstories2.APIs.SearchItemVk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.getAccessToken;
import static com.example.maxim.shortstories2.MyApplication.vkClient;
import static com.example.maxim.shortstories2.APIs.VkStrings.*;

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
        long beforeGet = new Date().getTime();

        List<Post> posts;
        try {
            List<PostVk> postsVK = vkClient
                    .getPosts(VERSION_API, getAccessToken(), id, updated)
                    .execute()
                    .body()
                    .response;
            posts = toPosts(postsVK);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
        posts = withRatio(posts);

        updated = beforeGet;
        new DBHelper().insertWall(this);
        (new DBHelper()).insertPosts(posts);
        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
        return true;
    }

    private List<Post> toPosts(List<PostVk> postsVK) {
        List<Post> posts = new ArrayList<>();
        for (PostVk aPostsVK : postsVK) {
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
        return posts;
    }

    public static List<SearchItem> searchWalls(String query) {
        Log.d("searchWalls", query);
        try {
            List<SearchItemVk> searchItemsVk = vkClient
                    .searchWalls(VERSION_API, getAccessToken(), query, 20, 1)
                    .execute()
                    .body()
                    .response;
            return toSearchItems(searchItemsVk);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<SearchItem> toSearchItems(List<SearchItemVk> searchItemsVk) {
        if (searchItemsVk == null) {
            return new ArrayList<>();
        }
        List<SearchItem> res = new ArrayList<>();
        for (SearchItemVk searchItemVk : searchItemsVk) {
            res.add(searchItemVk.toSearchItem());
        }
        return res;
    }
}
