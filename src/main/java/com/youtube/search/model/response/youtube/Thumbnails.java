package com.youtube.search.model.response.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Thumbnails {

    @JsonProperty("default")
    private Dimension defaultValue;
    @JsonProperty("medium")
    private Dimension mediumValue;
    @JsonProperty("high")
    private Dimension highValue;
}
