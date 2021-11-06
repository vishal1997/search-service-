package com.youtube.search.dao;

import com.youtube.search.exception.OperationNotSupported;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.model.VideoDetails;
import com.youtube.search.model.response.VideoDataResponse;
import com.youtube.search.model.response.youtube.Item;

import java.util.List;

public interface YoutubeDataDao {

    VideoDataResponse search(String query, String pageId) throws YoutubeSearchException;
    VideoDataResponse get(String pageId) throws YoutubeSearchException;
    void put(List<Item> items);
    void put(Item item) throws OperationNotSupported;
    void delete(List<Item> items) throws OperationNotSupported;
    void delete(Item item) throws OperationNotSupported;
}
