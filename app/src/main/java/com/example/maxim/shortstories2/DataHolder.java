package com.example.maxim.shortstories2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataHolder {
    public static List<WallVk> walls = new ArrayList<>();
    static {
        walls.addAll(Arrays.asList(new WallVk("Подслушано"), new WallVk("Just Story")));
    }
}
