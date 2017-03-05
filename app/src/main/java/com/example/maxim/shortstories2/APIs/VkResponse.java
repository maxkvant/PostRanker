package com.example.maxim.shortstories2.APIs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class VkResponse<T> {
    @SerializedName(VkStrings.JSON_RESPONSE)
    public final T response;

    @JsonCreator
    public VkResponse(@JsonProperty(VkStrings.JSON_RESPONSE) T response) {
        this.response = response;
    }
}
