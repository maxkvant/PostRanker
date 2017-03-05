package com.example.maxim.shortstories2.APIs;

import com.example.maxim.shortstories2.walls.SearchItem;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VkSearchItem implements Serializable {
    public final String description;
    public final Group group;
    public final Profile profile;

    @JsonCreator
    private VkSearchItem(@JsonProperty(VkStrings.JSON_DESCRIPTION) String description,
                         @JsonProperty(VkStrings.GROUP_ITEM_TYPE) Group group,
                         @JsonProperty(VkStrings.PROFILE_iTEM_TYPE) Profile profile) {
        this.description = description;
        this.group = group;
        this.profile = profile;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Group {
        public final long id;
        public final String name;

        @JsonCreator
        private Group(@JsonProperty(VkStrings.JSON_ID) long id,
                      @JsonProperty(VkStrings.JSON_NAME) String name) {
            this.id = id;
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        public final long id;
        public final String first_name;
        public final String last_name;

        @JsonCreator
        private Profile(@JsonProperty(VkStrings.JSON_ID) long id,
                        @JsonProperty(VkStrings.JSON_FIRST_NAME) String first_name,
                        @JsonProperty(VkStrings.JSON_LAST_NAME) String last_name) {
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
