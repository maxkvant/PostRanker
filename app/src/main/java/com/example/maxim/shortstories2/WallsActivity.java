package com.example.maxim.shortstories2;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.DataHolder.walls;

public class WallsActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walls);
        setTitle("Walls");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ListView listView = (ListView)findViewById(R.id.walls_list);
        listView.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = ((TextView)view).getText().toString();
            WallVk curWall = new WallVk(text.substring(2));
            if (text.substring(0, 1).equals("-")) {
                walls.remove(new WallVk(text.substring(2)));
            }
            if (text.substring(0, 1).equals("+")) {
                if (!walls.contains(curWall)) {
                    walls.add(curWall);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        Log.d("Create", "search menu");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ListView listView = (ListView)findViewById(R.id.walls_list);
        List<String> search_walls = new ArrayList<>(Arrays.asList("Подслушано", "Just Story", "New Story"));
        newText = newText.toLowerCase();
        List<String> list = new ArrayList<>();
        for (String name : search_walls) {
            boolean has = false;

            if (newText.length() > 1 && name.toLowerCase().contains(newText)) {
                String marker = "+ ";
                for (WallVk wall : DataHolder.walls) {
                    if (wall.toString().equals(name)) {
                        marker = "- ";
                    }
                }
                list.add(marker + name);
            }
        }

        Collections.sort(list);
        Collections.reverse(list);


        if (list.size() == 0) {
            list.add("Ничего не надено");
        }

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, list));
        listView.setOnItemClickListener(new ItemClickListener());
        return false;
    }
}