package com.example.maxim.shortstories2.APIs;

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
        @GET("/1.1/users/searchWalls.json")
        Call<List<User>> users(@Query("q") String query);
    }
}