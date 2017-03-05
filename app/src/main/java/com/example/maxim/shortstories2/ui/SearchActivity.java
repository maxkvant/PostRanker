package com.example.maxim.shortstories2.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.util.Callback;
import com.example.maxim.shortstories2.util.Consumer;
import com.example.maxim.shortstories2.walls.WallFactory;
import com.example.maxim.shortstories2.walls.SearchItem;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.util.Strings.FACTORY_WALL_INTENT;


public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private String lastText = "";
    private LinearLayout progressBarFill;
    private ListView wallsList;
    private Helper helper = new Helper();
    private WallFactory factoryWall;
    private boolean canFinish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBarFill = (LinearLayout) findViewById(R.id.progress_bar_fill);
        ((TextView) findViewById(R.id.progress_bar_fill_text)).setText(R.string.adding);

        factoryWall = (WallFactory) getIntent().getSerializableExtra(FACTORY_WALL_INTENT);

        setTitle(getString(R.string.search));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        wallsList = (ListView) findViewById(R.id.walls_list);
        wallsList.setOnItemClickListener(new ItemClickListener());

        onQueryTextChange("");
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
    public void finish() {
        if (canFinish) {
            super.finish();
        } else {
            Toast.makeText(this, "дождитесь завершения добавления", Toast.LENGTH_LONG).show();
        }

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
        helper.searchWalls(query, new Consumer<List<SearchItem>>() {
            @Override
            public void accept(List<SearchItem> searchItems) {
                if (searchItems == null) {
                    searchItems = Collections.singletonList(new SearchItem(getString(R.string.nothing_found), 0, ""));
                }
                ListAdapter adapterSearchList = new SearchItemAdapter(SearchActivity.this, searchItems);
                wallsList.setAdapter(adapterSearchList);
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        lastText = newText;
        return false;
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SearchItem searchItem = (SearchItem) parent.getItemAtPosition(position);
            helper.addWall(
                searchItem
                , new Runnable() {
                        @Override
                        public void run() {
                            canFinish = false;
                            progressBarFill.setVisibility(View.VISIBLE);
                            wallsList.setVisibility(View.GONE);
                        }
                    }
                , new Runnable() {
                        @Override
                        public void run() {
                            wallsList.setVisibility(View.VISIBLE);
                            progressBarFill.setVisibility(View.GONE);
                            canFinish = true;
                        }
                    });
        }
    }

    public class Helper {
        public void addWall(SearchItem searchItem, final Runnable beforeAdd, final Runnable afterAdd) {
            final Wall wall = factoryWall.toWall(searchItem);
            beforeAdd.run();
            wall.update(new Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    new DBHelper().insertWall(wall);
                    afterAdd.run();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SearchActivity.this, R.string.add_wall_failed, Toast.LENGTH_LONG).show();
                    afterAdd.run();
                }
            });
        }

        public void searchWalls(String query, final Consumer<List<SearchItem>> afterSearch) {
            factoryWall.searchWalls(query, new Callback<List<SearchItem>>() {
                @Override
                public void onSuccess(List<SearchItem> result) {
                    afterSearch.accept(result);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SearchActivity.this, R.string.search_failed, Toast.LENGTH_LONG).show();
                }
            });
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