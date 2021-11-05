package com.youtube.search.model.response.youtube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Item {

    private String kind;
    private String etag;
    private Id id;
    private Snippet snippet;
}
