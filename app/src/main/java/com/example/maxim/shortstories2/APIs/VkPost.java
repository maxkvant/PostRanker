package com.example.maxim.shortstories2.APIs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VkPost {
    public final long id;

    public final String text;

    public final Integer date;

    public final Likes likes;

    @JsonCreator
    private VkPost(@JsonProperty(VkStrings.JSON_ID) long id,
                   @JsonProperty(VkStrings.JSON_TEXT) String text,
                   @JsonProperty(VkStrings.JSON_DATE) Integer date,
                   @JsonProperty(VkStrings.JSON_LIKES) Likes likes) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.likes = likes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Likes {
        public final int count;

        @JsonCreator
        private Likes(@JsonProperty(VkStrings.JSON_COUNT) int count) {
            this.count = count;
        }
    }
}