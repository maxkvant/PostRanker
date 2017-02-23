package com.example.maxim.shortstories2.walls;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.example.maxim.shortstories2.walls.VkStrings.*;

public class SearchItemVk implements Serializable {
    @SerializedName(JSON_DESCRIPTION)
    public String description;

    @SerializedName(GROUP_ITEM_TYPE)
    public Group group;

    @SerializedName(PROFILE_iTEM_TYPE)
    public Profile profile;

    public static class Group {
        @SerializedName(JSON_ID)
        long id;

        @SerializedName(JSON_NAME)
        String name;
    }

    public static class Profile {
        @SerializedName(JSON_ID)
        long id;

        @SerializedName(JSON_FIRST_NAME)
        String first_name;

        @SerializedName(JSON_LAST_NAME)
        String last_name;
    }
}
