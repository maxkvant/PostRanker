package com.example.maxim.shortstories2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.post.PostsAdapter;
import com.example.maxim.shortstories2.util.Consumer;
import com.example.maxim.shortstories2.util.SharedPrefs;
import com.example.maxim.shortstories2.walls.WallMode;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;
import static com.example.maxim.shortstories2.util.Strings.FIRST_RUN;
import static com.example.maxim.shortstories2.util.Strings.TRUE;
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
    List<WallMode> modes = Arrays.asList(WallMode.values());
    private Helper helper;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walls.addAll(new DBHelper().getAllWalls());
        helper = new Helper(walls.get(0), BY_DATE);

        initToolbar();
        initSpinner();
        initDrawer();
        initSwipeRefresh();

        if (SharedPrefs.getString(this, FIRST_RUN) == null) {
            SharedPrefs.storeString(this, FIRST_RUN, TRUE);
            helper.refresh(new Runnable() {
                @Override
                public void run() {

                }
            });
        };
        Log.d("onCreate, walls-size", walls.size() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        walls.clear();
        walls.addAll(new DBHelper().getAllWalls());
        adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initToolbar() {
        setContentView(R.layout.activity_main);
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
                startActivityForResult(intent, 1);
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (0 < position && position <= walls.size()) {
                helper = new Helper(walls.get(position - 1), BY_DATE);
                setPostsAdapter();
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
            spinnerItems.add(mapModes.get(mode));
        }

        ArrayAdapter adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerItems);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new SpinnerItemClickListener());
    }

    private class SpinnerItemClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            helper = new Helper(helper.wall, modes.get(position));
            setPostsAdapter();
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
                helper.refresh(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        setPostsAdapter();
                    }
                });
            }
        });

        footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar, null);
    }

    private void setPostsAdapter() {
        drawer.closeDrawers();
        spinner.setSelection(modes.indexOf(helper.mode));
        final ListView feed = (ListView) findViewById(R.id.feed_list);
        final PostsAdapter adapter = new PostsAdapter(this);
        feed.setAdapter(adapter);
        feed.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == feed.getCount() - 1) {
                    feed.addFooterView(footerView);
                    helper.getPosts(new Consumer<List<Post>>() {
                        @Override
                        public void accept(List<Post> posts) {
                            feed.removeFooterView(footerView);
                            adapter.addPosts(posts);
                        }
                    });
                }
            }
        });
    }

    public static class Helper {
        private boolean hasAsyncTask;
        public final Wall wall;
        public final WallMode mode;
        private int count;

        Helper(Wall wall, WallMode mode) {
            this.wall = wall;
            this.mode = mode;
        }

        public void refresh(final Runnable onRefresh) {
            count = 0;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Log.d("MainActivity", "before update");
                    wall.update();
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    onRefresh.run();
                }
            }.execute();
        }

        public void getPosts(final Consumer<List<Post> > onGetPosts) {
            if (!hasAsyncTask) {
                hasAsyncTask = true;

                new AsyncTask<Void, Void, List<Post>>() {
                    private final int count = Helper.this.count;
                    @Override
                    protected List<Post> doInBackground(Void... walls) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("Helper::getPosts", count + "");
                        return wall.getPosts(count, mode);
                    }

                    @Override
                    protected void onPostExecute(List<Post> result) {
                        Helper.this.count += result.size();
                        onGetPosts.accept(result);
                        hasAsyncTask = false;
                    }
                }.execute();
            }
        }
    }
}