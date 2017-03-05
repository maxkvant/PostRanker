package com.example.maxim.shortstories2.APIs;

import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.maxim.shortstories2.APIs.VkStrings.*;

public interface VkClient {
    @GET(METHOD_GET_POSTS)
    Call<ResponseBody> getPosts(
            @Query(PARAM_NAME_VERSION) String version,
            @Query(PARAM_NAME_ACCESS_TOKEN) String accessToken,
            @Query(PARAM_NAME_ID) long id,
            @Query(PARAM_NAME_DATE) Long date);

    @GET(METHOD_SEARCH_WALLS)
    Call<ResponseBody> searchWalls(
            @Query(PARAM_NAME_VERSION) String version,
            @Query(PARAM_NAME_ACCESS_TOKEN) String accessToken,
            @Query(PARAM_NAME_QUERY) String query,
            @Query(PARAM_NAME_LIMIT) Integer limit,
            @Query(PARAM_NAME_SEARCH_TYPE) Integer is_search_global);
}
