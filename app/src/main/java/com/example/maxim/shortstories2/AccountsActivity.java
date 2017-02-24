package com.example.maxim.shortstories2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.walls.FactoryWallTwitter;
import com.example.maxim.shortstories2.walls.FactoryWallVk;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;
import static com.example.maxim.shortstories2.Strings.FACTORY_WALL_INTENT;

public class AccountsActivity extends AppCompatActivity {
    private TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonWallsVk = (Button) findViewById(R.id.button_walls_vk);
        buttonWallsVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountsActivity.this, WallsActivity.class);
                intent.putExtra(FACTORY_WALL_INTENT, new FactoryWallVk());
                startActivity(intent);
            }
        });

        Button buttonWallsTwitter = (Button) findViewById(R.id.button_walls_twitter);
        buttonWallsTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountsActivity.this, WallsActivity.class);
                intent.putExtra(FACTORY_WALL_INTENT, new FactoryWallTwitter());
                startActivity(intent);
            }
        });


        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterApiClient = new MyTwitterApiClient(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
