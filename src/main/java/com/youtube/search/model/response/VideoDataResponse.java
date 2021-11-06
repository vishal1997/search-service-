package com.youtube.search.model.response;

import com.youtube.search.model.VideoDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class VideoDataResponse {

    private String pageId;
    private List<VideoDetails> videoDetails;
}
