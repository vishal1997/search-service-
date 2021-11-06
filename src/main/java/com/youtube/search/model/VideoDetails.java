package com.youtube.search.model;

import com.youtube.search.model.response.youtube.Thumbnails;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VideoDetails {

    private String videoId;
    private Date publishedAt;
    private String channelId;
    private String title;
    private String description;
    private Thumbnails thumbnails;
    private String channelTitle;
    private Date publishTime;
}
