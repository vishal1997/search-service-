package com.youtube.search.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.youtube.search.exception.YoutubeResponseParseException;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.model.SearchRequest;
import com.youtube.search.model.response.youtube.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
public class YoutubeClient {

    private final HttpClient httpClient;
    private final SearchAppConfig config;
    private final ObjectMapper objectMapper;

    @Inject
    public YoutubeClient(@Named("youtubeHttpClient") CloseableHttpClient httpClient, SearchAppConfig config,
                         ObjectMapper objectMapper) {
        this.httpClient = new HttpClient(httpClient);
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public SearchResponse search(SearchRequest searchRequest) throws YoutubeSearchException, YoutubeResponseParseException {

        Map<String, String> queryParam = Maps.newHashMap();
        queryParam.put("key", config.getGoogleSearchApiKey());
        queryParam.put("q", searchRequest.getQuery());
        queryParam.put("type", searchRequest.getType());
        queryParam.put("part", StringUtils.join(searchRequest.getPart(), ","));
        queryParam.put("publishedAfter", searchRequest.getPublishedAfter());

        String response = null;
        log.info("Youtube Query: {}", searchRequest.getQuery());
        try {
            response = httpClient.get(config.getGoogleSearchApi(), queryParam, HttpClient.headers());
        } catch (URISyntaxException e) {
            log.error("Error while creating youtube url, {}, {}", e.getMessage(), e.getReason());
            throw new YoutubeSearchException("Youtube URI ERROR", e);
        } catch (IOException e) {
            log.error("Error while closing youtube http connection, {}, {}", e.getMessage(), e.getCause());
            throw new YoutubeSearchException("Youtube Http Connection Error", e);
        } catch (HttpException e) {
            log.error("Youtube http request failure, {}, {}", e.getMessage(), e.getCause());
            throw new YoutubeSearchException(e.getMessage(), e);
        }

        try {
            SearchResponse searchResponse = objectMapper.readValue(response, SearchResponse.class);
            return searchResponse;
        } catch (IOException e) {
            log.error("Error while parsing youtubr response, {}, {}", e.getMessage(), e.getCause());
            throw new YoutubeResponseParseException("response:" + response, e);
        }
    }
}
