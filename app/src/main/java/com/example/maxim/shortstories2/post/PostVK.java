package com.example.maxim.shortstories2.post;

import com.example.maxim.shortstories2.walls.VkStrings;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Downloader;
import com.twitter.sdk.android.core.models.BindingValues;

public class PostVK {

        @SerializedName(VkStrings.JSON_TEXT)
        public final String text;

        @SerializedName(VkStrings.JSON_DATE)
        public final Integer date;

        @SerializedName(VkStrings.JSON_LIKES)
        public final Likes likes;

        private PostVK(String text, Integer date, Likes likes) {
            this.text = text;
            this.date = date;
            this.likes = likes;
        }


        public static class Likes {
            @SerializedName(VkStrings.JSON_COUNT)
            public final Integer count;

            private Likes(Integer count) {
                this.count = count;
            }
        }
}