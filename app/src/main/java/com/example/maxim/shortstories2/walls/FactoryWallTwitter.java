package com.example.maxim.shortstories2.walls;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;

public class FactoryWallTwitter extends AbstractFactoryWall {
    static final String className = FactoryWallTwitter.class.getSimpleName();

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new WallTwitter(name, id, ratio, updated);
    }

    @Override
    public  List<SearchItem> searchWalls(String query) {
        final MyTwitterApiClient client = twitterApiClient;
        Call<List<User>> usersCall = client.getSearchUsersService().users(query);

        List<SearchItem> res = null;
        List<User> users = null;
        try {
            users = usersCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (users != null) {
            res = new ArrayList<>();
            for (User user : users) {
                res.add(new SearchItem(user.name, user.getId(), user.screenName));
            }
        }
        return res;
    }
}

class WallTwitter extends AbstractWall {
    private final int maxTweetsPerGet = 200;
    private final int iterations = 8;

    public WallTwitter(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public List<Post> getPosts(int offset, WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(offset, mode, id);
    }

    @Override
    public boolean update() {
        final MyTwitterApiClient client = twitterApiClient;
        Long maxId = null;
        List<Post> posts = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        long curDate = cal.getTimeInMillis() / 1000;
        cal.add(Calendar.MONTH, -1);
        int minDate = (int) (cal.getTimeInMillis() / 1000);

        for (int i = 0; i < iterations; i++) {
            Call<List<Tweet>> listCall = client
                    .getStatusesService()
                    .userTimeline(id,
                            null,
                            maxTweetsPerGet,
                            null,
                            maxId,
                            null,
                            null,
                            null,
                            null);

            List<Tweet> tweets = new ArrayList<>();

            try {
                Response<List<Tweet>> listResponse = listCall.execute();
                tweets.addAll(listResponse.body());
                posts.addAll(tweetsToPosts(tweets));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (posts.size() == 0) {
                return false;
            }
            if (tweets.size() != 0) {
                maxId = tweets.get(tweets.size() - 1).id - 1;
            }

            int lastDate = posts.get(posts.size() - 1).date;
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

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public String getFactoryClassName() {
        return FactoryWallTwitter.className;
    }

    private List<Post> tweetsToPosts(List<Tweet> tweets) {
        List<Post> posts = new ArrayList<>();
        for (Tweet tweet : tweets) {
            int date = (int) (Date.parse(tweet.createdAt) / 1000);
            posts.add(new Post(
                    tweet.id,
                    tweet.text,
                    id,
                    name,
                    date,
                    tweet.favoriteCount,
                    FactoryWallTwitter.className));
        }
        return posts;
    }

}
