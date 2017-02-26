package com.example.maxim.shortstories2.APIs;

import com.google.gson.annotations.SerializedName;

public class VkPost {
    @SerializedName(VkStrings.JSON_ID)
    public final long id;

    @SerializedName(VkStrings.JSON_TEXT)
        public final String text;

        @SerializedName(VkStrings.JSON_DATE)
        public final Integer date;

        @SerializedName(VkStrings.JSON_LIKES)
        public final Likes likes;

        private VkPost(long id, String text, Integer date, Likes likes) {
            this.id = id;
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