package com.youtube.search.managd;

import com.google.inject.Inject;
import com.youtube.search.client.YoutubeClient;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.manager.YoutubeDataManager;
import com.youtube.search.manager.impl.YoutubeDataManagerImpl;
import com.youtube.search.runnable.YoutubeDataManagerRunnable;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Create a fixed scheduler which will run in background to get data from youtube and put in ES
 */
public class YoutubeDataScheduler implements Managed {

    private final SearchAppConfig config;
    private final YoutubeDataManager youtubeDataManager;

    @Inject
    public YoutubeDataScheduler(SearchAppConfig config, YoutubeDataManager youtubeDataManager) {
        this.config = config;
        this.youtubeDataManager = youtubeDataManager;
    }

    @Override
    public void start() throws Exception {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(config.getSchedulerConfig().getThreadPool());
        scheduler.scheduleAtFixedRate(new YoutubeDataManagerRunnable(youtubeDataManager, config), config.getSchedulerConfig().getInitialDelay(),
                config.getSchedulerConfig().getPeriod(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() throws Exception {

    }
}
