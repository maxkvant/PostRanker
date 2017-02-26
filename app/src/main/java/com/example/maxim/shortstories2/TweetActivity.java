package com.example.maxim.shortstories2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.maxim.shortstories2.post.Post;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.util.Strings.POST_INTENT;

public class TweetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Post post = (Post) getIntent().getSerializableExtra(POST_INTENT);

        final List<Long> tweetIds = Collections.singletonList(post.id);
        final LinearLayout mLayout = (LinearLayout) findViewById(R.id.m_layout);

        TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                for (Tweet tweet : result.data) {
                    Log.d("TweetActivity", tweet.text);
                    mLayout.addView(new TweetView(TweetActivity.this, tweet));

                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TweetActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
