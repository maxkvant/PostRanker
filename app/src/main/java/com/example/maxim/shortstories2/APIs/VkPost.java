package com.example.maxim.shortstories2.APIs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VkPost {
    @SerializedName(VkStrings.JSON_ID)
    public final long id;

    @SerializedName(VkStrings.JSON_TEXT)
    public final String text;

    @SerializedName(VkStrings.JSON_DATE)
    public final Integer date;

    @SerializedName(VkStrings.JSON_LIKES)
    public final Likes likes;

    @SerializedName(VkStrings.JSON_COPY_HISTORY)
    public final List<VkPost> copy_history;

    private VkPost(long id, String text, Integer date, Likes likes, List<VkPost> copy_history) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.likes = likes;
        this.copy_history = copy_history;
    }

    public static class Likes {
        @SerializedName(VkStrings.JSON_COUNT)
        public final Integer count;

        private Likes(Integer count) {
            this.count = count;
        }
    }
}