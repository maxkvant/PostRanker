package com.example.maxim.shortstories2.post;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.maxim.shortstories2.MainActivity;
import com.example.maxim.shortstories2.PostActivity;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.walls.WALL_MODE;

import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

public class PostsAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<Post> items;

    public PostsAdapter(Context context) {
        this.ctx = context;
        this.items = new ArrayList<>();
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            convertView = inflater.inflate(R.layout.feed_item, null);
        }
        final Post item = items.get(position);
        initView(convertView, item);
        Button buttonComment = (Button) convertView.findViewById(R.id.feed_item_comment);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, PostActivity.class);
                intent.putExtra("Post", item);
                ctx.startActivity(intent);
            }
        });
        return convertView;
    }

    public static void initView(View convertView, Post item) {
        ((TextView) convertView.findViewById(R.id.feed_item_wall)).setText(item.wall_name);
        ((TextView) convertView.findViewById(R.id.feed_item_text)).setText(item.text);
        ((TextView) convertView.findViewById(R.id.feed_item_date))
                .setText(String.valueOf(new Date((long) item.date * 1000)));
    }

    public void addPosts(List<Post> list) {
        items.addAll(list);
        this.notifyDataSetChanged();
    }
}
