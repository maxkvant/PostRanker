package com.example.maxim.shortstories2.post;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.maxim.shortstories2.DBHelper;
import com.example.maxim.shortstories2.ui.TweetActivity;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.ui.VkPostActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.maxim.shortstories2.util.Strings.POST_INTENT;

public class PostsAdapter extends CursorAdapter {
    private Activity activity;

    public PostsAdapter(Activity activity, Cursor cursor) {
        super(activity, cursor, true);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Post post = new DBHelper().toPost(cursor);
        initView(view, post);
        Button button = (Button) view.findViewById(R.id.feed_item_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class newActivity;
                Intent intent;
                switch (post.factoryWall) {
                    case "TwitterWallFactory":
                        newActivity = TweetActivity.class;
                        break;
                    case "VkWallFactory":
                        newActivity = VkPostActivity.class;
                        break;
                    default:
                        newActivity = null;
                }
                if (newActivity != null) {
                    intent = new Intent(PostsAdapter.this.activity, newActivity);
                    intent.putExtra(POST_INTENT, post);
                    activity.startActivity(intent);
                }
            }
        });
    }

    private static void initView(View convertView, Post item) {
        ((TextView) convertView.findViewById(R.id.feed_item_wall)).setText(item.wall_name);
        ((TextView) convertView.findViewById(R.id.feed_item_text)).setText(item.text);
        ((TextView) convertView.findViewById(R.id.feed_item_date))
                .setText(String.valueOf(new Date((long) item.date * 1000)));
    }
}
