package com.example.maxim.shortstories2.walls;

import com.google.gson.annotations.SerializedName;

public class VkResponse<T> {
    @SerializedName(VkStrings.JSON_RESPONSE)
    public final T response;

    public VkResponse(T response) {
        this.response = response;
    }
}
