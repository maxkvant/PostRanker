package com.example.maxim.shortstories2.walls;

import android.database.Cursor;
import android.util.Log;

import com.example.maxim.shortstories2.APIs.VkPost;
import com.example.maxim.shortstories2.APIs.VkResponse;
import com.example.maxim.shortstories2.APIs.VkSearchItem;
import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.util.Strings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.sdk.VKServiceActivity;
import com.vk.sdk.api.VKError;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.maxim.shortstories2.APIs.VkStrings.VERSION_API;
import static com.example.maxim.shortstories2.MyApplication.getAccessToken;
import static com.example.maxim.shortstories2.MyApplication.vkClient;
import static com.example.maxim.shortstories2.util.Strings.lineSeparator;

public class VkWallFactory extends AbstractWallFactory {
    static final String className = TwitterWallFactory.class.getSimpleName();

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new VkWall(name, id, ratio, updated);
    }

    @Override
    public List<SearchItem> searchWalls(String query) throws Exception {
        Log.d("searchWalls", query);
        Response<VkResponse<List<VkSearchItem>>> response = vkClient
                .searchWalls(VERSION_API, getAccessToken(), query, 20, 1)
                .execute();

        if (response.isSuccessful() && response.body().response != null) {
            List<VkSearchItem> searchItemsVk = response.body().response;
            return toSearchItems(searchItemsVk);
        } else {
            throw new Exception("search failed");
        }
    }

    private static List<SearchItem> toSearchItems(List<VkSearchItem> searchItemsVk) {
        if (searchItemsVk == null) {
            return new ArrayList<>();
        }
        List<SearchItem> res = new ArrayList<>();
        for (VkSearchItem vkSearchItem : searchItemsVk) {
            res.add(vkSearchItem.toSearchItem());
        }
        return res;
    }
}

class VkWall extends AbstractWall {
    public VkWall(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public Cursor getPosts(WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(mode, id);
    }

    @Override
    public void update() throws Exception {
        long beforeGet = new Date().getTime();

        List<VkPost> postsVK = vkClient
                .getPosts(VERSION_API, getAccessToken(), id, updated)
                .execute()
                .body()
                .response;
        List<Post> posts = toPosts(postsVK);

        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
        posts = withRatio(posts);

        updated = beforeGet;
        new DBHelper().insertWall(this);
        (new DBHelper()).insertPosts(posts);
        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
    }

    private List<Post> toPosts(List<VkPost> postsVK) {
        final String for_repost = lineSeparator + lineSeparator +
                "➡ ➡ " + Strings.REPOST + ":" +
                lineSeparator;

        List<Post> posts = new ArrayList<>();
        if (postsVK == null) {
            return posts;
        }
        for (VkPost vkPost : postsVK) {
            String text = "";
            if (vkPost.text != null) {
                text = vkPost.text.replace("\\\n", System.getProperty("line.separator"));
            }

            StringBuilder resText = new StringBuilder(text);
            if (vkPost.copy_history != null) {
                for (VkPost vkRepost : vkPost.copy_history) {
                    String curText = "";
                    if (vkRepost.text != null) {
                        curText = vkRepost.text.replace("\\\n", lineSeparator);
                    }
                    resText.append(for_repost)
                            .append(curText);
                }
            }

            double rating = vkPost.likes.count;
            rating = rating * rating;
            posts.add(new Post(
                    vkPost.id,
                    resText.toString(),
                    id,
                    name,
                    vkPost.date,
                    rating,
                    VkWallFactory.className));
        }
        return posts;
    }

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public String getFactoryClassName() {
        return VkWallFactory.class.getSimpleName();
    }
}