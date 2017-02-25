package com.example.maxim.shortstories2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.maxim.shortstories2.APIs.MyTwitterApiClient;
import com.example.maxim.shortstories2.walls.FactoryWallTwitter;
import com.example.maxim.shortstories2.walls.FactoryWallVk;
import com.example.maxim.shortstories2.walls.Wall;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import static com.example.maxim.shortstories2.WallsActivity.ButtonAction.NULL;
import static com.example.maxim.shortstories2.MyApplication.twitterApiClient;
import static com.example.maxim.shortstories2.MyApplication.walls;
import static com.example.maxim.shortstories2.util.Strings.FACTORY_WALL_INTENT;

public class WallsActivity extends AppCompatActivity {
    private TwitterLoginButton twitterLoginButton;
    private ListView wallsList;
    private ArrayAdapter<Wall> adapterWallsList;
    enum ButtonAction {ADD_WALL, VK_LOGIN, NULL}
    private ButtonAction curButton = NULL;
    LinearLayout headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        wallsList = (ListView) findViewById(R.id.walls_list);
        wallsList.setOnItemClickListener(new ItemClickListener());
        adapterWallsList = new ArrayAdapter<>(this, R.layout.wall_list_item, walls);
        wallsList.setAdapter(adapterWallsList);

        headerView = (LinearLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.activity_walls_header, null);

        wallsList.addHeaderView(headerView);

        initVkButtons();
        initTwitterButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (curButton) {
            case VK_LOGIN:
                VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                    @Override
                    public void onResult(VKAccessToken res) {
                        MyApplication.setAccessToken(res.accessToken);
                    }

                    @Override
                    public void onError(VKError error) {
                    }
                });
            case ADD_WALL:
                adapterWallsList.notifyDataSetChanged();
            case NULL:
                twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
        curButton = NULL;

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


    private void initVkButtons() {
        Button buttonLoginVk = (Button) headerView.findViewById(R.id.button_login_vk);
        buttonLoginVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curButton = ButtonAction.VK_LOGIN;
                VKSdk.login(WallsActivity.this, "friends", "groups");
            }
        });

        Button buttonWallsVk = (Button) headerView.findViewById(R.id.button_walls_vk);
        buttonWallsVk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallsActivity.this, SearchActivity.class);
                intent.putExtra(FACTORY_WALL_INTENT, new FactoryWallVk());
                curButton = ButtonAction.ADD_WALL;
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initTwitterButtons() {
        twitterLoginButton = (TwitterLoginButton) headerView.findViewById(R.id.button_login_twitter);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterApiClient = new MyTwitterApiClient(result.data);
            }

            @Override
            public void failure(TwitterException exception) {}
        });

        Button buttonWallsTwitter = (Button) headerView.findViewById(R.id.button_walls_twitter);

        buttonWallsTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallsActivity.this, SearchActivity.class);
                intent.putExtra(FACTORY_WALL_INTENT, new FactoryWallTwitter());
                curButton = ButtonAction.ADD_WALL;
                startActivityForResult(intent, 1);
            }
        });
    }

    private void deleteWall(Wall wall) {
        DBHelper dbHelper = new DBHelper();
        dbHelper.deleteWall(wall.getId());
        walls.clear();
        walls.addAll(dbHelper.getAllWalls());
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            synchronized (walls) {
                if (0 < position && position <= walls.size()) {
                    deleteWall(walls.get(position - 1));
                }
            }
            adapterWallsList.notifyDataSetChanged();
        }
    }
}
