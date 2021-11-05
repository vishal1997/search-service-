package com.youtube.search.dao;

import com.youtube.search.exception.OperationNotSupported;
import com.youtube.search.model.response.youtube.Item;

import java.util.List;

public interface YoutubeDataDao {

    List<Item> search(String query);
    void put(List<Item> items);
    void put(Item item) throws OperationNotSupported;
    void delete(List<Item> items) throws OperationNotSupported;
    void delete(Item item) throws OperationNotSupported;
}
