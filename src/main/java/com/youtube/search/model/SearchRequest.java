package com.youtube.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SearchRequest {

    private String query;
    private String type;
    private List<String> part;
    private String publishedAfter;
}
