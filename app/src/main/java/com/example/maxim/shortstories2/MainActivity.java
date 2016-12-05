package com.example.maxim.shortstories2;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.post.PostsAdapter;
import com.example.maxim.shortstories2.walls.Wall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.maxim.shortstories2.MyApplication.walls;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("Feed");

        ListAdapter adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main);

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

        Button buttonUpdate = (Button) findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Wall wall : walls) {
                    setPostsAdapter(Collections.<Post> emptyList());
                    wall.update();
                }
            }
        });

        setPostsAdapter(walls.get(1).getPosts());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ListAdapter adapterDrawer = new ArrayAdapter<>(this, R.layout.drawer_item, walls);
        ListView leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setAdapter(adapterDrawer);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setPostsAdapter(walls.get(position).getPosts());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    private void setPostsAdapter(List<Post> posts) {
        ListView feed = (ListView)findViewById(R.id.feed_list);
        PostsAdapter adapter = new PostsAdapter(getApplicationContext(), posts);
        feed.setAdapter(adapter);
    }
}
