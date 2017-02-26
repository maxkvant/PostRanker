package com.example.maxim.shortstories2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.maxim.shortstories2.APIs.VkPost;
import com.example.maxim.shortstories2.post.Post;

import static com.example.maxim.shortstories2.util.Strings.M_VK_BASE_URL;
import static com.example.maxim.shortstories2.util.Strings.POST_INTENT;

public class VkPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Post post = (Post) getIntent().getSerializableExtra(POST_INTENT);

        WebView webView = (WebView) findViewById(R.id.web_view_vk_post);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(VkPostActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });

        String url = M_VK_BASE_URL + post.wall_id + "_" + post.id;
        webView.loadUrl(url);
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
}
