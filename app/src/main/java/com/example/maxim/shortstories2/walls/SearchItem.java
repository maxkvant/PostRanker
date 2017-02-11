package com.example.maxim.shortstories2.walls;

public class SearchItem {
    public final String name;
    public final long id;
    public final String description;
    public SearchItem(String name, long id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
    }
    @Override
    public String toString() {
        return name;
    }
}
