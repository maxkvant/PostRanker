package com.example.maxim.shortstories2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maxim.shortstories2.post.Comment;
import com.example.maxim.shortstories2.post.Post;
import com.example.maxim.shortstories2.post.PostsAdapter;

import java.security.PublicKey;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private Post post;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        post = (Post) getIntent().getSerializableExtra("Post");
        View postView = findViewById(R.id.feed_item);
        PostsAdapter.initView(postView, post);

        editText = (EditText) findViewById(R.id.comment_text);

        List<Comment> comments = new DBHelper().getComments(post.id);
        if (comments.size() == 0) {
            editText.setText(getResources().getString(R.string.default_comment));
        } else {
            editText.setText(comments.get(comments.size() - 1).text);
        }

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        new DBHelper().insertComment(new Comment(post.id, editText.getText().toString(), post.id));
        finish();
    }

}
