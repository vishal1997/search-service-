package com.youtube.search.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.youtube.search.model.SearchRequest;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAppConfig extends Configuration {

    @NotNull
    @JsonProperty("swagger")
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @JsonProperty("youtubeHttpConfig")
    private HttpConnectionConfig youtubeHttpConfig;

    @NotNull
    private SchedulerConfig schedulerConfig;

    @NotNull
    private String googleSearchApi;

    @NotNull
    private String googleSearchApiKey;

    @NotNull
    private SearchRequest searchRequest;

    @NotNull
    @JsonProperty("elasticsearch")
    private List<String> elasticsearchHosts;

    @NotNull
    private String indexName;
}
