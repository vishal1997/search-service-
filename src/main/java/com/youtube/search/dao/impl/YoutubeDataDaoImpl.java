package com.youtube.search.dao.impl;

import com.google.inject.Inject;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.exception.OperationNotSupported;
import com.youtube.search.model.response.youtube.Item;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

public class YoutubeDataDaoImpl implements YoutubeDataDao {

    private final RestHighLevelClient client;

    @Inject
    public YoutubeDataDaoImpl(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public List<Item> search(String query) {
        return null;
    }

    @Override
    public void put(List<Item> items) {

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
