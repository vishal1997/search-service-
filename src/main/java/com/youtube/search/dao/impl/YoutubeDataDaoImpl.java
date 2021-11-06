package com.youtube.search.dao.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.exception.OperationNotSupported;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.model.VideoDetails;
import com.youtube.search.model.response.VideoDataResponse;
import com.youtube.search.model.response.youtube.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Youtube Data layer
 *
 * TODO: Hava a separate class with all the constants
 */
@Slf4j
public class YoutubeDataDaoImpl implements YoutubeDataDao {

    private final RestHighLevelClient client;
    private final SearchAppConfig config;
    private final ObjectMapper objectMapper;
    private final String FIELDS[] = { "title", "title.keyword", "description", "description.keyword"};

    @Inject
    public YoutubeDataDaoImpl(RestHighLevelClient client, SearchAppConfig config, ObjectMapper objectMapper) {
        this.client = client;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @Override
    public VideoDataResponse search(String query, String pageId) throws YoutubeSearchException {

        /**
         * TODO If else block can be eliminated using factory pattern
         */
        SearchResponse searchResponse;
        if (StringUtils.isEmpty(pageId)) {
            SearchRequest searchRequest = searchRequestBuilder(query);
            searchResponse = search(searchRequest);
        } else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(pageId);
            scrollRequest.scroll(TimeValue.timeValueMinutes(1L));
            searchResponse = search(scrollRequest);
        }
        return fromSearchResponse(searchResponse);
    }

    private SearchResponse search(SearchScrollRequest scrollRequest) throws YoutubeSearchException {
        try {
            SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            return searchScrollResponse;
        } catch (IOException e) {
            throw new YoutubeSearchException("Search fetching ES data", e);
        }
    }

    @Override
    public VideoDataResponse get(String pageId) throws YoutubeSearchException {

        /**
         * TODO Similar to search method can be modified to remove duplicate code
         */
        SearchResponse searchResponse;
        if (StringUtils.isEmpty(pageId)) {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.sort("publishTime", SortOrder.DESC);
            searchResponse = search(searchRequest());
        } else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(pageId);
            scrollRequest.scroll(TimeValue.timeValueMinutes(1L));
            searchResponse = search(scrollRequest);
        }

        return fromSearchResponse(searchResponse);
    }

    @Override
    public void put(List<Item> items) {

        BulkRequest bulkRequest = new BulkRequest();

        List<IndexRequest> list = items.stream()
                .map(e -> fromItem(e))
                .map(e -> rawData(e))
                .filter(e -> e != null)
                .map(e -> generateIndexRequest(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        list.forEach(e ->  bulkRequest.add(e));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            /**
             * TODO Ignoring exception in the background process for now. This can be put in some store to have a track of all the failure and look into fix.
             */
            log.error("Exception while inserting video data to Elasticsearch: {}", e);
        }
    }

    @Override
    public void put(Item item) throws OperationNotSupported {
        throw new OperationNotSupported("Single item addition");
    }

    @Override
    public void delete(List<Item> items) throws OperationNotSupported {
        throw new OperationNotSupported("Delete operation not supported");
    }

    @Override
    public void delete(Item item) throws OperationNotSupported {
        throw new OperationNotSupported("Single item deletion");
    }

    private SearchRequest searchRequest() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(config.getIndexName());
        searchRequest.scroll(TimeValue.timeValueMinutes(1L));
        return searchRequest;
    }

    private SearchResponse search(SearchRequest searchRequest) throws YoutubeSearchException {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse;
        } catch (IOException e) {
            throw new YoutubeSearchException("Search fetching ES data", e);
        }
    }

    private VideoDataResponse fromSearchResponse(SearchResponse searchResponse) {
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<VideoDetails> videoDetailsList = Lists.newArrayList();

        for (SearchHit searchHit : searchHits) {
            try {
                VideoDetails videoDetails = objectMapper.readValue(searchHit.getSourceAsString(), VideoDetails.class);
                videoDetailsList.add(videoDetails);
            } catch (IOException e) {
                // TODO Ignoring it for now
                log.error("Not able to serialize data ");
            }
        }

        return VideoDataResponse.builder()
                .pageId(searchResponse.getScrollId())
                .videoDetails(videoDetailsList)
                .build();
    }

    private SearchRequest searchRequestBuilder(String query) {

        MultiMatchQueryBuilder queryBuilder = new MultiMatchQueryBuilder(query, FIELDS);
        queryBuilder.fuzziness(Fuzziness.fromEdits(2));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);

        SearchRequest searchRequest = searchRequest();
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private IndexRequest generateIndexRequest(String key, String data) {
        return new IndexRequest(config.getIndexName()).id(key).opType(DocWriteRequest.OpType.CREATE).source(data, XContentType.JSON);
    }

    private Pair<String, String> rawData(VideoDetails videoDetails) {
        try {
            return Pair.of(videoDetails.getVideoId(), objectMapper.writeValueAsString(videoDetails));
        } catch (IOException e) {
            log.error("Error while serializing data");
            return null;
        }
    }

    private VideoDetails fromItem(Item item) {
        return VideoDetails.builder()
                .channelId(item.getSnippet().getChannelId())
                .channelTitle(item.getSnippet().getChannelTitle())
                .description(item.getSnippet().getDescription())
                .publishedAt(item.getSnippet().getPublishedAt())
                .publishTime(item.getSnippet().getPublishTime())
                .thumbnails(item.getSnippet().getThumbnails())
                .title(item.getSnippet().getTitle())
                .videoId(item.getId().getVideoId())
                .build();
    }

}
