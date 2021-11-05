package com.youtube.search.resource;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/search")
@Api("Youtube search api")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class SearchResource {

    @GET
    public Response search(@QueryParam("q") String query) {

        return Response.ok().build();
    }
}
