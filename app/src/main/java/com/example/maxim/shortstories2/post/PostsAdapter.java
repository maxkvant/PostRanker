package com.example.maxim.shortstories2.post;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.maxim.shortstories2.ui.TweetActivity;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.ui.VkPostActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.maxim.shortstories2.util.Strings.POST_INTENT;

public class PostsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> items;

    public PostsAdapter(Activity activity) {
        this.activity = activity;
        this.items = new ArrayList<>();
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        Button button = (Button) convertView.findViewById(R.id.feed_item_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class newActivity;
                Intent intent;
                switch (item.factoryWall) {
                    case "FactoryWallTwitter":
                        newActivity = TweetActivity.class;
                        break;
                    case "FactoryWallVk":
                        newActivity = VkPostActivity.class;
                        break;
                    default:
                        newActivity = null;
                }
                if (newActivity != null) {
                    intent = new Intent(PostsAdapter.this.activity, newActivity);
                    intent.putExtra(POST_INTENT, item);
                    activity.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private static void initView(View convertView, Post item) {
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
