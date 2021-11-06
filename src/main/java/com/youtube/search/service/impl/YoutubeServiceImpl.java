package com.youtube.search.service.impl;

import com.google.inject.Inject;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.model.response.VideoDataResponse;
import com.youtube.search.service.YoutubeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YoutubeServiceImpl implements YoutubeService {

    private YoutubeDataDao youtubeDataDao;

    @Inject
    public YoutubeServiceImpl(YoutubeDataDao youtubeDataDao) {
        this.youtubeDataDao = youtubeDataDao;
    }

    @Override
    public VideoDataResponse search(String query, String pageId) throws YoutubeSearchException {
        return youtubeDataDao.search(query, pageId);
    }

    @Override
    public VideoDataResponse get(String pageId) throws YoutubeSearchException {
        return youtubeDataDao.get(pageId);
    }
}
