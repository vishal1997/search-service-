package com.youtube.search.managd;

import com.google.inject.Inject;
import com.youtube.search.client.YoutubeClient;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.manager.impl.YoutubeDataManagerImpl;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class YoutubeDataScheduler implements Managed {

    private SearchAppConfig config;
    private YoutubeClient youtubeClient;

    @Inject
    public YoutubeDataScheduler(SearchAppConfig config, YoutubeClient youtubeClient) {
        this.config = config;
        this.youtubeClient = youtubeClient;
    }

    @Override
    public void start() throws Exception {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(config.getSchedulerConfig().getThreadPool());
        scheduler.scheduleAtFixedRate(new YoutubeDataManagerImpl(config, youtubeClient), config.getSchedulerConfig().getInitialDelay(),
                config.getSchedulerConfig().getPeriod(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() throws Exception {

    }
}
