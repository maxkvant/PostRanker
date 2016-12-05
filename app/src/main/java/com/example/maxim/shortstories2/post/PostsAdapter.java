package com.example.maxim.shortstories2.post;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maxim.shortstories2.R;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater inflater;
    private List<Post> items;

    public PostsAdapter(Context context, List<Post> items) {
        this.ctx = context;
        this.items = items;
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
        Post item = items.get(position);
        ((TextView) convertView.findViewById(R.id.feed_item_wall)).setText(item.wall);
        ((TextView) convertView.findViewById(R.id.feed_item_text)).setText(item.text);
        ((TextView) convertView.findViewById(R.id.feed_item_date))
                .setText(String.valueOf(new Date((long)item.date * 1000)));
        return convertView;
    }
}
