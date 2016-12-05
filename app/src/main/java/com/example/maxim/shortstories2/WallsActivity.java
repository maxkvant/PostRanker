package com.example.maxim.shortstories2;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maxim.shortstories2.walls.Wall;
import com.example.maxim.shortstories2.walls.WallVk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;

public class WallsActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener {
    private String lastText = "";

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
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("name", "");
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
        newText = newText.toLowerCase();
        ListView listView = (ListView)findViewById(R.id.walls_list);

        List<String> search_walls = new ArrayList<>(Arrays.asList("Подслушано", "Just Story", "New Story", "Убойные Истории"));

        newText = newText.toLowerCase();
        List<String> list = new ArrayList<>();
        for (String name : search_walls) {
            if (newText.length() > 1 && name.toLowerCase().contains(newText)) {
                String marker = "+ ";
                for (Wall wall : walls) {
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

        listView.setAdapter(new ArrayAdapter<>(this, R.layout.search_list_item, list));
        listView.setOnItemClickListener(new ItemClickListener());
        return false;
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = ((TextView)view).getText().toString();
            String name = text.substring(2, text.length());
            if (text.substring(0, 1).equals("-")) {
                for (int i = 0; i < walls.size(); i++) {
                    if (walls.get(i).toString().equals(name)) {
                        walls.get(i).deletePosts();
                        walls.remove(i);
                        break;
                    }
                }
            }
            if (text.substring(0, 1).equals("+")) {
                walls.add(new WallVk(name));
            }
            onQueryTextChange(lastText);
        }
    }
}