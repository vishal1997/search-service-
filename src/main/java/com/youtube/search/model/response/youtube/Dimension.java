package com.youtube.search.model.response.youtube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Dimension {

    private String url;
    private int width;
    private int height;
}
