package com.example.maxim.shortstories2.APIs;

import com.example.maxim.shortstories2.walls.SearchItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VkSearchItem implements Serializable {
    @SerializedName(VkStrings.JSON_DESCRIPTION)
    public final String description;

    @SerializedName(VkStrings.GROUP_ITEM_TYPE)
    public final Group group;

    @SerializedName(VkStrings.PROFILE_iTEM_TYPE)
    public final Profile profile;

    private VkSearchItem(String description, Group group, Profile profile) {
        this.description = description;
        this.group = group;
        this.profile = profile;
    }

    public static class Group {
        @SerializedName(VkStrings.JSON_ID)
        public final long id;

        @SerializedName(VkStrings.JSON_NAME)
        public final String
                name;

        private Group(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Profile {
        @SerializedName(VkStrings.JSON_ID)
        public final long id;

        @SerializedName(VkStrings.JSON_FIRST_NAME)
        public final String first_name;

        @SerializedName(VkStrings.JSON_LAST_NAME)
        public final String last_name;

        private Profile(long id, String first_name, String last_name) {
            this.id = id;
            this.first_name = first_name;
            this.last_name = last_name;
        }
    }

    public SearchItem toSearchItem() {
        String name;
        long id;
        if (profile != null) {
            id = profile.id;
            name = profile.last_name + " " + profile.first_name;
        } else {
            id = -group.id;
            name = group.name;
        }
        return new SearchItem(name, id, description);
    }
}
