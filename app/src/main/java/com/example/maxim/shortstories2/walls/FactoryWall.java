package com.example.maxim.shortstories2.walls;

import java.io.Serializable;
import java.util.List;

public interface FactoryWall extends Serializable {
    Wall create(String name, long id, double ratio, long updated);
    List<SearchItem> searchWalls(String query);
    Wall toWall(SearchItem searchItem);
}

abstract class AbstractFactoryWall implements FactoryWall {
    @Override
    public Wall toWall(SearchItem searchItem) {
        return  create(searchItem.name, searchItem.id, 0, 0);
    }
}