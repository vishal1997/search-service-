package com.youtube.search.resource;


import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.youtube.search.exception.YoutubeSearchException;
import com.youtube.search.model.VideoDetails;
import com.youtube.search.model.response.VideoDataResponse;
import com.youtube.search.service.YoutubeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Api("Youtube search api")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class SearchResource {

    private final YoutubeService youtubeService;

    @Inject
    public SearchResource(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("q") String query, @QueryParam("pageId") String pageId) {

        try {
            VideoDataResponse videoDetailsLists = youtubeService.search(query, pageId);
            return Response.ok(videoDetailsLists).build();
        } catch (YoutubeSearchException e) {
            return Response.serverError().build();
        }
    }

    @GET
    public Response get(@QueryParam("pageId") String pageId) {

        try {
            VideoDataResponse videoDetailsLists = youtubeService.get(pageId);
            return Response.ok(videoDetailsLists).build();
        } catch (YoutubeSearchException e) {
            return Response.serverError().build();
        }
    }
}
