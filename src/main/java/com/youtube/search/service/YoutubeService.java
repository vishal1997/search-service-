package com.youtube.search.service;

import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.model.VideoDetails;
import com.youtube.search.model.response.VideoDataResponse;

import java.util.List;

public interface YoutubeService {

    VideoDataResponse search(String query, String pageId) throws YoutubeSearchException;
    VideoDataResponse get(String pageId) throws YoutubeSearchException;
}
