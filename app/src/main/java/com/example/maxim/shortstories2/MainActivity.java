package com.example.maxim.shortstories2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
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
import android.widget.ListView;
import android.widget.Spinner;

import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.post.PostsAdapter;
import com.example.maxim.shortstories2.walls.WALL_MODE;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;
import static com.example.maxim.shortstories2.walls.WALL_MODE.BY_DATE;
import static com.example.maxim.shortstories2.walls.WALL_MODE.TOP_DAILY;
import static com.example.maxim.shortstories2.walls.WALL_MODE.TOP_MONTHLY;
import static com.example.maxim.shortstories2.walls.WALL_MODE.TOP_WEEKLY;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private ArrayAdapter adapterDrawer;
    private boolean hasAsyncTask = false;
    private View footerView;
    private Wall currentWall;
    private WALL_MODE currentMode = BY_DATE;
    private Spinner spinner;
    private final List<WALL_MODE> modes = Arrays.asList(WALL_MODE.values());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walls = (new DBHelper()).getAllWalls();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        spinner = (Spinner)findViewById(R.id.spinner_nav);

        EnumMap<WALL_MODE,String> mapModes = new EnumMap<WALL_MODE, String>(WALL_MODE.class);
        Resources res = getResources();
        mapModes.put(BY_DATE, getResources().getString(R.string.by_date));
        mapModes.put(TOP_DAILY, getResources().getString(R.string.top_daily));
        mapModes.put(TOP_WEEKLY, getResources().getString(R.string.top_weekly));
        mapModes.put(TOP_MONTHLY, getResources().getString(R.string.top_monthly));

        List<String> spinnerItems = new ArrayList<>();
        for (WALL_MODE mode : modes) {
            spinnerItems.add(mapModes.get(mode));
        }

        ArrayAdapter adapterSpinner = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerItems);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new SpinnerItemClickListener());


        adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView)findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        final DrawerLayout drawer = (DrawerLayout)findViewById(R.id.activity_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        leftDrawer.setOnItemClickListener(new DrawerItemClickListener());

        Button buttonWalls = (Button) findViewById(R.id.button_goto_walls);
        buttonWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WallsActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivityForResult(intent, 1);
            }
        });

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new  SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                     Log.d("MainActivity", "onRefresh");

                    refreshLayout.setRefreshing(true);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Log.d("MainActivity", "before update");
                            currentWall.update();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            hasAsyncTask = false;
                            refreshLayout.setRefreshing(false);
                            setPostsAdapter(currentWall);
                        }
                    }.execute();
            }
        });


        footerView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.progress_bar, null);


        setPostsAdapter(walls.get(1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapterDrawer.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            currentMode = BY_DATE;
            setPostsAdapter(walls.get(position));
        }
    }

    private class SpinnerItemClickListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentMode = modes.get(position);
            setPostsAdapter(currentWall);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    private void setPostsAdapter(final Wall wall) {
        spinner.setSelection(modes.indexOf(currentMode));
        final ListView feed = (ListView)findViewById(R.id.feed_list);
        final PostsAdapter adapter = new PostsAdapter(getApplicationContext(), currentMode);
        feed.setAdapter(adapter);
        new WallScroll(feed, wall, adapter).execute();
        currentWall = wall;
        feed.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!hasAsyncTask && view.getLastVisiblePosition() == feed.getCount() - 1) {
                    new WallScroll(feed, wall, adapter).execute();
                }
            }
        });
    }

    private class WallScroll extends AsyncTask<Void, Void, List<Post>> {
        private final PostsAdapter adapter;
        private final ListView feed;
        private final Wall wall;
        private final int count;

        WallScroll(ListView feed, Wall wall, PostsAdapter adapter) {
            this.feed = feed;
            this.wall = wall;
            this.adapter = adapter;
            this.count = adapter.getCount();
            hasAsyncTask = true;
        }
        @Override
        protected void onPreExecute () {
            hasAsyncTask = true;
            feed.addFooterView(footerView);
        }

        @Override
        protected List<Post> doInBackground (Void ...walls){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return wall.getPosts(count, adapter.mode);
        }

        @Override
        protected void onPostExecute (List<Post> result) {
            feed.removeFooterView(footerView);
            adapter.addPosts(result);
            hasAsyncTask = false;
        }
    }
}
