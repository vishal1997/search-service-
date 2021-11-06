package com.youtube.search.manager.impl;

import com.google.inject.Inject;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.exception.YoutubeResponseParseException;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.client.YoutubeClient;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.manager.YoutubeDataManager;
import com.youtube.search.model.SearchRequest;
import com.youtube.search.model.response.youtube.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class YoutubeDataManagerImpl implements YoutubeDataManager {

    private SearchAppConfig searchAppConfig;
    private YoutubeClient youtubeClient;
    private YoutubeDataDao youtubeDataDao;

    @Inject
    public YoutubeDataManagerImpl(SearchAppConfig searchAppConfig, YoutubeClient youtubeClient, YoutubeDataDao youtubeDataDao) {

        this.youtubeClient = youtubeClient;
        this.searchAppConfig = searchAppConfig;
        this.youtubeDataDao = youtubeDataDao;
    }


    @Override
    public void populateData(SearchRequest searchRequest) {

        try {

            Date date = Date.from(new Date().toInstant().minusSeconds(searchAppConfig.getSchedulerConfig().getPeriod()));

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            String dateAsISOString = df.format(date);
            searchRequest = SearchRequest.builder()
                    .part(searchAppConfig.getSearchRequest().getPart())
                    .publishedAfter(dateAsISOString)
                    .query(searchAppConfig.getSearchRequest().getQuery())
                    .type(searchAppConfig.getSearchRequest().getType())
                    .build();

            SearchResponse searchResponse = youtubeClient.search(searchRequest);
            youtubeDataDao.put(searchResponse.getItems());
        } catch (YoutubeSearchException | YoutubeResponseParseException e) {
            log.error("Error for query {}", searchRequest);
        }
    }
}
