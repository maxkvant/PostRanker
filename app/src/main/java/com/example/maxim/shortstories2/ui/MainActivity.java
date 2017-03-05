package com.example.maxim.shortstories2.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.post.PostsAdapter;
import com.example.maxim.shortstories2.util.AsyncCall;
import com.example.maxim.shortstories2.util.CallableException;
import com.example.maxim.shortstories2.util.Callback;
import com.example.maxim.shortstories2.util.Consumer;
import com.example.maxim.shortstories2.walls.WallMode;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static com.example.maxim.shortstories2.walls.WallMode.BY_DATE;
import static com.example.maxim.shortstories2.walls.WallMode.COMMENTED;
import static com.example.maxim.shortstories2.walls.WallMode.TOP_DAILY;
import static com.example.maxim.shortstories2.walls.WallMode.TOP_MONTHLY;
import static com.example.maxim.shortstories2.walls.WallMode.TOP_WEEKLY;
import static com.example.maxim.shortstories2.walls.WallMode.TOP_ALL;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private ArrayAdapter adapterDrawer;
    private View footerView;
    private Spinner spinner;
    private List<WallMode> modes = Arrays.asList(WallMode.values());
    private Helper helper;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private final DBHelper dbHelper = new DBHelper();
    private List<Wall> walls;
    private AsyncCall<Void> updateCall = null;
    private PostsAdapter adapter;
    private ListView feed;

    private Runnable beforeGetPosts;
    
    private Consumer<Cursor> afterGetPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        init();
        initSpinner();
        initDrawer();
        initSwipeRefresh();

        Log.d("onCreate, walls-size", walls.size() + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        walls = dbHelper.getAllWalls();
        adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        walls = dbHelper.getAllWalls();
        feed = (ListView) findViewById(R.id.feed_list);
        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar, null);

        beforeGetPosts = new Runnable() {
            @Override
            public void run() {
                feed.addFooterView(footerView);
            }
        };

        afterGetPosts = new Consumer<Cursor>() {
            @Override
            public void accept(Cursor cursor) {
                if (cursor != null) {
                    setPostsAdapter(cursor);
                }
                feed.removeFooterView(footerView);
            }
        };

        helper = new Helper(walls.get(0), BY_DATE);
        helper.getPosts(beforeGetPosts, afterGetPosts);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initDrawer() {
        adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        drawer = (DrawerLayout) findViewById(R.id.activity_main);
        leftDrawer.setOnItemClickListener(new DrawerItemClickListener());

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout headerView = (LinearLayout) inflater
                .inflate(R.layout.drawer_header, null);
        leftDrawer.addHeaderView(headerView);

        Button buttonWalls = (Button) headerView.findViewById(R.id.button_goto_walls);
        buttonWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
                Intent intent = new Intent(MainActivity.this, WallsActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (0 < position && position <= walls.size()) {
                helper = new Helper(walls.get(position - 1), BY_DATE);
                helper.getPosts(beforeGetPosts, afterGetPosts);
            }
        }
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_nav);
        EnumMap<WallMode,String> mapModes = new EnumMap<>(WallMode.class);
        mapModes.put(BY_DATE, getResources().getString(R.string.by_date));
        mapModes.put(TOP_DAILY, getResources().getString(R.string.top_daily));
        mapModes.put(TOP_WEEKLY, getResources().getString(R.string.top_weekly));
        mapModes.put(TOP_MONTHLY, getResources().getString(R.string.top_monthly));
        mapModes.put(TOP_ALL, getResources().getString(R.string.top_all));
        mapModes.put(COMMENTED, getResources().getString(R.string.commented));

        List<String> spinnerItems = new ArrayList<>();
        for (WallMode mode : modes) {
            if (mode != COMMENTED) {
                spinnerItems.add(mapModes.get(mode));
            }
        }

        ArrayAdapter adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerItems);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new SpinnerItemClickListener());
    }

    private class SpinnerItemClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            helper = new Helper(helper.wall, modes.get(position));
            helper.getPosts(beforeGetPosts, afterGetPosts);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void initSwipeRefresh() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new  SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("MainActivity", "onRefresh");
                refreshLayout.setRefreshing(true);
                updateCall = helper.wall.update(new Callback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        refreshLayout.setRefreshing(false);
                        updateCall = null;
                        helper.getPosts(beforeGetPosts, afterGetPosts);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        refreshLayout.setRefreshing(false);
                        updateCall = null;
                        Toast.makeText(MainActivity.this, R.string.update_failed, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setPostsAdapter(Cursor cursor) {
        drawer.closeDrawers();
        spinner.setSelection(modes.indexOf(helper.mode));
        if (adapter != null) {
            adapter.getCursor().close();
        }
        adapter = new PostsAdapter(this, cursor);

        feed.setAdapter(adapter);

        if (updateCall != null) {
            updateCall.cancel(true);
            updateCall = null;
            Toast.makeText(MainActivity.this, R.string.update_cancel, Toast.LENGTH_LONG).show();
            refreshLayout.setRefreshing(false);
        }
    }

    public static class Helper {
        private boolean hasAsyncTask;
        public final Wall wall;
        public final WallMode mode;

        public Helper(Wall wall, WallMode mode) {
            this.wall = wall;
            this.mode = mode;
        }

        public void getPosts(final Runnable beforeGet, final Consumer<Cursor> afterGet) {
            if (!hasAsyncTask) {
                hasAsyncTask = true;
                beforeGet.run();

                wall.getPosts(mode, new Callback<Cursor>() {
                    @Override
                    public void onSuccess(Cursor result) {
                        afterGet.accept(result);
                        hasAsyncTask = false;
                    }

                    @Override
                    public void onFailure(Exception e) {
                        afterGet.accept(null);
                        hasAsyncTask = false;
                    }
                });
            }
        }
    }
}