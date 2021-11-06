package com.youtube.search.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.exception.OperationNotSupported;
import com.youtube.search.model.VideoDetails;
import com.youtube.search.model.response.youtube.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class YoutubeDataDaoImpl implements YoutubeDataDao {

    private final RestHighLevelClient client;
    private SearchAppConfig config;
    private ObjectMapper objectMapper;

    @Inject
    public YoutubeDataDaoImpl(RestHighLevelClient client, SearchAppConfig config, ObjectMapper objectMapper) {
        this.client = client;
        this.config = config;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<Item> search(String query) {

        return null;
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
            log.error("Exception while inserting video data to Elasticsearch: {}", e);
        }

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
}
