package com.youtube.search.manager.impl;

import com.google.inject.Inject;
import com.youtube.exception.YoutubeResponseParseException;
import com.youtube.exception.YoutubeSearchException;
import com.youtube.search.client.YoutubeClient;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.manager.SearchManager;
import com.youtube.search.model.SearchRequest;
import com.youtube.search.model.response.youtube.SearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
public class YoutubeSearchManager implements SearchManager, Runnable {

    private SearchAppConfig searchAppConfig;
    private YoutubeClient youtubeClient;

    @Inject
    public YoutubeSearchManager(SearchAppConfig searchAppConfig, YoutubeClient youtubeClient) {

        this.youtubeClient = youtubeClient;
        this.searchAppConfig = searchAppConfig;
    }


    @Override
    public SearchResponse search(SearchRequest searchRequest) {

        try {
            return youtubeClient.search(searchRequest);
        } catch (YoutubeSearchException | YoutubeResponseParseException e) {
            log.error("Error for query {}", searchRequest);
        }
        return null;
    }

    @Override
    public void run() {

        Date date = Date.from(new Date().toInstant().minusSeconds(searchAppConfig.getSchedulerConfig().getPeriod()));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String dateAsISOString = df.format(date);
        SearchRequest searchRequest = SearchRequest.builder()
                .part(searchAppConfig.getSearchRequest().getPart())
                .publishedAfter(dateAsISOString)
                .query(searchAppConfig.getSearchRequest().getQuery())
                .type(searchAppConfig.getSearchRequest().getType())
                .build();
        SearchResponse searchResponse = search(searchRequest);

        log.info("{}", searchResponse);
    }
}
