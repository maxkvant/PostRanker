package com.example.maxim.shortstories2;
import org.json.JSONException;

import java.util.List;

public interface Wall {
    public List<Post> getPosts() throws JSONException;
    public void deletePosts();
}
