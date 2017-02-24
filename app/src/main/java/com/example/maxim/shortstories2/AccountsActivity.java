package com.example.maxim.shortstories2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;
import static com.example.maxim.shortstories2.util.Strings.FACTORY_WALL_INTENT;

public class AccountsActivity extends AppCompatActivity {
    private TwitterLoginButton twitterLoginButton;
    private boolean onVkLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initVkButtons();
        initTwitterButtons();
    }

    private void initVkButtons() {
        Button buttonLoginVk = (Button) findViewById(R.id.button_login_vk);
        buttonLoginVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVkLogin = true;
                VKSdk.login(AccountsActivity.this, "friends", "groups");
            }
        });

        Button buttonWallsVk = (Button) findViewById(R.id.button_walls_vk);
        buttonWallsVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountsActivity.this, WallsActivity.class);
                intent.putExtra(FACTORY_WALL_INTENT, new FactoryWallVk());
                startActivity(intent);
            }
        });
    }

    private void initTwitterButtons() {
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.button_login_twitter);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterApiClient = new MyTwitterApiClient(result.data);
            }

            @Override
            public void failure(TwitterException exception) {}
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (onVkLogin) {
            VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                @Override
                public void onResult(VKAccessToken res) {
                    MyApplication.setAccessToken(res.accessToken);
                }

                @Override
                public void onError(VKError error) {
                }
            });
            onVkLogin = false;
        }

        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            super.onResume();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
