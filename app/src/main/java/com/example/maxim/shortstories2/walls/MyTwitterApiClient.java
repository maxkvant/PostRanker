package com.example.maxim.shortstories2.walls;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MyTwitterApiClient extends TwitterApiClient {
    public SearchUsersService getSearchUsersService() {
        return getService(SearchUsersService.class);
    }

    public interface SearchUsersService {
        @GET("/1.1/users/search.json")
        Call<List<User>> users(@Query("q") String query);
    }
}
