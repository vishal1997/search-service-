package com.youtube.search.model.response.youtube;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PageInfo {

    private int totalResults;
    private int resultsPerPage;
}
