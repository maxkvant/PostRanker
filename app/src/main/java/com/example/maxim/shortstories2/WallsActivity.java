package com.example.maxim.shortstories2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maxim.shortstories2.walls.FactoryWall;
import com.example.maxim.shortstories2.walls.SearchItem;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;
import static com.example.maxim.shortstories2.Strings.FACTORY_WALL_INTENT;


public class WallsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private String lastText = "";
    private LinearLayout progressBarFill;
    ListView wallsList;
    ArrayAdapter adapterWallsList;
    boolean wasSearch = false;
    private Helper helper = new Helper();
    private FactoryWall factoryWall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walls);

        progressBarFill = (LinearLayout) findViewById(R.id.progress_bar_fill);
        ((TextView) findViewById(R.id.progress_bar_fill_text)).setText(R.string.adding);

        factoryWall = (FactoryWall) getIntent().getSerializableExtra(FACTORY_WALL_INTENT);

        setTitle("Walls");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        wallsList = (ListView) findViewById(R.id.walls_list);
        wallsList.setOnItemClickListener(new ItemClickListener());

        adapterWallsList = new ArrayAdapter<>(this, R.layout.wall_list_item, walls);
        wallsList.setAdapter(adapterWallsList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        lastText = newText;
        helper.searchWalls(newText, new Consumer<List<SearchItem>>() {
            @Override
            public void accept(List<SearchItem> searchItems) {
                if (searchItems == null) {
                    searchItems = Collections.singletonList(new SearchItem("Ничего не найдено", 0, ""));
                }
                ListAdapter adapterSearchList = new SearchItemAdapter(WallsActivity.this, searchItems);
                wallsList.setAdapter(adapterSearchList);
                wasSearch = true;
            }
        });
        return false;
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (wasSearch) {
                SearchItem searchItem = (SearchItem) parent.getItemAtPosition(position);
                    helper.addWall(
                            searchItem
                            , new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBarFill.setVisibility(View.VISIBLE);
                                        wallsList.setVisibility(View.GONE);
                                    }
                                }
                            , new Runnable() {
                                    @Override
                                    public void run() {
                                        wallsList.setVisibility(View.VISIBLE);
                                        progressBarFill.setVisibility(View.GONE);
                                    }
                                });

                helper.deleteWall(walls.get(position), new Runnable() {
                    @Override
                    public void run() {
                        ((Vibrator) WallsActivity.this.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);
                        adapterWallsList.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public class Helper {
        public void deleteWall(Wall wall, Runnable afterWallsDeleted) {
            DBHelper dbHelper = new DBHelper();
            dbHelper.deleteWall(wall.getId());
            walls.clear();
            walls.addAll(dbHelper.getAllWalls());
            afterWallsDeleted.run();
        }

        public void addWall(SearchItem searchItem, final Runnable beforeAdd, final Runnable afterAdd) {
            final Wall wall = factoryWall.toWall(searchItem);

            new AsyncTask<Void,Void,Void>() {
                @Override
                protected void onPreExecute() {
                    beforeAdd.run();
                }
                @Override
                protected Void doInBackground(Void... params) {
                    DBHelper dbHelper = new DBHelper();
                    if (wall.update()) {
                        dbHelper.insertWall(wall);
                    }
                    walls = dbHelper.getAllWalls();
                    return null;
                }
                @Override
                protected void onPostExecute(Void result) {
                    afterAdd.run();
                }
            }.execute();
        }

        public void searchWalls(String query, Consumer<List<SearchItem>> afterSearch) {
            new SearchTask(query, afterSearch).execute();
        }

        private class SearchTask extends AsyncTask<Void, Void, List<SearchItem> > {
            final String query;
            final Consumer<List<SearchItem>> afterSearch;

            SearchTask(String query, Consumer<List<SearchItem>> afterSearch) {
                this.query = query;
                this.afterSearch = afterSearch;
            }

            @Override
            protected List<SearchItem> doInBackground(Void... params) {
                return factoryWall.searchWalls(query);
            }

            @Override
            protected void onPostExecute(List<SearchItem> result) {
                afterSearch.accept(result);
            }
        }
    }
}

class SearchItemAdapter extends BaseAdapter {
    private final List<SearchItem> items;
    private LayoutInflater inflater;

    SearchItemAdapter(Context context, List<SearchItem> lst) {
        items = lst;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_item, null, false);
        }
        SearchItem item = items.get(position);
        ((TextView) convertView.findViewById(R.id.search_item_name)).setText(item.name);
        ((TextView) convertView.findViewById(R.id.search_item_description)).setText(item.description);
        return convertView;
    }
}