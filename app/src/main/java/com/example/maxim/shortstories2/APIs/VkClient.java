package com.example.maxim.shortstories2.APIs;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkClient {
    @GET("/method/execute.getPostsMonthlySince")
    Call<VkResponse<List<VkPost>>> getPosts(
            @Query(VkStrings.PARAM_NAME_VERSION) String version,
            @Query(VkStrings.PARAM_NAME_ACCESS_TOKEN) String accessToken,
            @Query(VkStrings.PARAM_NAME_ID) long id,
            @Query(VkStrings.PARAM_NAME_DATE) Long date);

    @GET("/method/search.getHints")
    Call<VkResponse<List<VkSearchItem>>> searchWalls(
            @Query(VkStrings.PARAM_NAME_VERSION) String version,
            @Query(VkStrings.PARAM_NAME_ACCESS_TOKEN) String accessToken,
            @Query(VkStrings.PARAM_NAME_QUERY) String query,
            @Query(VkStrings.PARAM_NAME_LIMIT) Integer limit,
            @Query(VkStrings.PARAM_NAME_SEARCH_TYPE) Integer is_search_global);
}
