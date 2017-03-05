package com.example.maxim.shortstories2.walls;

import android.database.Cursor;
import android.util.Log;

import com.example.maxim.shortstories2.APIs.VkPost;
import com.example.maxim.shortstories2.APIs.VkResponse;
import com.example.maxim.shortstories2.APIs.VkSearchItem;
import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.example.maxim.shortstories2.APIs.VkStrings.VERSION_API;
import static com.example.maxim.shortstories2.MyApplication.getAccessToken;
import static com.example.maxim.shortstories2.MyApplication.vkClient;

public class VkWallFactory extends AbstractWallFactory {
    static final String className = TwitterWallFactory.class.getSimpleName();

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new VkWall(name, id, ratio, updated);
    }

    @Override
    public List<SearchItem> searchWalls(String query) throws Exception {
        Log.d("searchWalls", query);
        InputStream searchItemsVkStream = vkClient
                .searchWalls(VERSION_API, getAccessToken(), query, 20, 1)
                .execute()
                .body()
                .byteStream();

        ObjectMapper mapper = new ObjectMapper();
        VkResponse<List<VkSearchItem> > vkResponse = mapper.readValue(searchItemsVkStream,
                new TypeReference<VkResponse<List<VkSearchItem>>>() {});
        List<VkSearchItem> searchItemsVk = vkResponse.response;

        return toSearchItems(searchItemsVk);
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

        InputStream postsVkStream = vkClient
                .getPosts(VERSION_API, getAccessToken(), id, updated)
                .execute()
                .body()
                .byteStream();

        ObjectMapper mapper = new ObjectMapper();
        VkResponse<VkPost []> vkResponse = mapper.readValue(postsVkStream,
                new TypeReference<VkResponse<VkPost[]>>() {});
        VkPost[] postsVK = vkResponse.response;
        List<Post> posts = toPosts(postsVK);

        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
        posts = withRatio(posts);

        updated = beforeGet;
        new DBHelper().insertWall(this);
        (new DBHelper()).insertPosts(posts);
        Log.d("WallVk::update time", (new Date().getTime() - beforeGet) + "");
    }

    private List<Post> toPosts(VkPost[] postsVK) {
        List<Post> posts = new ArrayList<>();
        if (postsVK == null) {
            return posts;
        }
        for (VkPost vkPost : postsVK) {
            String text = "";
            if (vkPost.text != null) {
                text = vkPost.text.replace("\\\n", System.getProperty("line.separator"));
            }
            double rating = vkPost.likes.count;
            rating = rating * rating;
            posts.add(new Post(
                    vkPost.id,
                    text,
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