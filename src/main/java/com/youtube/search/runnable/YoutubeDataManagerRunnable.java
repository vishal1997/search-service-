package com.youtube.search.runnable;

import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.manager.YoutubeDataManager;

public class YoutubeDataManagerRunnable implements Runnable {

    private final YoutubeDataManager youtubeDataManager;
    private final SearchAppConfig config;

    public YoutubeDataManagerRunnable(YoutubeDataManager youtubeDataManager, SearchAppConfig config) {
        this.youtubeDataManager = youtubeDataManager;
        this.config = config;
    }

    @Override
    public void run() {
        youtubeDataManager.populateData(config.getSearchRequest());
    }
}
