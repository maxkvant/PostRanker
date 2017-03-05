package com.example.maxim.shortstories2.walls;

import android.database.Cursor;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.post.Post;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;

public class TwitterWallFactory extends AbstractWallFactory {
    static final String className = TwitterWallFactory.class.getSimpleName();

    @Override
    public Wall create(String name, long id, double ratio, long updated) {
        return new TwitterWall(name, id, ratio, updated);
    }

    @Override
    public List<SearchItem> searchWalls(String query) throws Exception {
        final MyTwitterApiClient client = twitterApiClient;
        Call<List<User>> usersCall = client.getSearchUsersService().users(query);

        List<SearchItem> res = null;
        List<User> users = usersCall.execute().body();

        if (users != null) {
            res = new ArrayList<>();
            for (User user : users) {
                res.add(new SearchItem(user.name, user.getId(), user.screenName));
            }
        }
        return res;
    }
}

class TwitterWall extends AbstractWall {
    private final int maxTweetsPerGet = 200;
    private final int iterations = 8;

    public TwitterWall(String name, long id, double ratio, long updated) {
        super(name, id, ratio, updated);
    }

    @Override
    public Cursor getPosts(WallMode mode) {
        DBHelper dbHelper = new DBHelper();
        return dbHelper.getPosts(mode, id);
    }

    @Override
    public void update() throws Exception {
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

            Response<List<Tweet>> listResponse = listCall.execute();
            tweets.addAll(listResponse.body());
            posts.addAll(tweetsToPosts(tweets));

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
    }

    @Override
    public boolean isSource() {
        return true;
    }

    @Override
    public String getFactoryClassName() {
        return TwitterWallFactory.className;
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
                    TwitterWallFactory.className));
        }
        return posts;
    }

}
