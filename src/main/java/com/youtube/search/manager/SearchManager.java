package com.youtube.search.manager;

import com.youtube.search.model.SearchRequest;
import com.youtube.search.model.response.youtube.SearchResponse;

import java.util.List;

public interface SearchManager {

    SearchResponse search(SearchRequest searchRequest);
}
