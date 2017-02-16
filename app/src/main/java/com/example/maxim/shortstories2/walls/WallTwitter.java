package com.example.maxim.shortstories2.walls;

import android.util.Log;
import android.util.LongSparseArray;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.google.gson.JsonElement;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.internal.SwipeToDismissTouchListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;

public class WallTwitter extends AbstractWall {
    public WallTwitter(String name, long id, double ratio, long updated) {
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
        Long maxId = null;
        int maxTweets = 200;
        int lastDate = 0;
        int iterations = 8;

        List<Post> posts = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        long curDate = cal.getTimeInMillis() / 1000;
        cal.add(Calendar.MONTH, -1);
        int minDate = (int) (cal.getTimeInMillis() / 1000);

        for (int i = 0; i < iterations; i++) {
            Call<List<Tweet>> listCall = TwitterCore.getInstance().getGuestApiClient().getStatusesService()
                    .userTimeline(id
                            , null
                            , maxTweets
                            , null
                            , maxId
                            , null
                            , null
                            , null
                            , null);

            List<Tweet> curTweets = new ArrayList<>();
            try {
                Response<List<Tweet>> listResponse = listCall.execute();
                curTweets.addAll(listResponse.body());
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Tweet tweet : curTweets) {
                int date = (int) (Date.parse(tweet.createdAt) / 1000);
                lastDate = date;
                maxId = tweet.id - 1;
                posts.add(new Post(
                        tweet.text
                        , id
                        , name
                        , date
                        , tweet.favoriteCount
                ));
            }

            if (posts.size() == 0) {
                return false;
            }

            int seconds_in_day = 60 * 60 * 24;
            if (lastDate < updated - seconds_in_day || lastDate < minDate) {
                break;
            }
        }

        updated = curDate;

        posts = withRatio(posts);
        DBHelper dbHelper = new DBHelper();
        dbHelper.insertWall(this);
        dbHelper.insertPosts(posts);
        return true;
    }

    public static List<SearchItem> search(String query) {
        MyTwitterApiClient client = new MyTwitterApiClient();
        MyTwitterApiClient.SearchUsersService searchUsersService = client.getSearchUsersService();
        Call<List<User>> usersCall = twitterApiClient.getSearchUsersService().users(query);

        List<SearchItem> res = new ArrayList<>();
        List<User> users = null;
        try {
            users = usersCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (users != null) {
            for (User user : users) {
                res.add(new SearchItem(user.name, user.getId(), user.screenName));
            }
        }
        return res;
    }
}
