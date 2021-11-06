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

    private String googleSearchApi;

    private String googleSearchApiKey;

    private SearchRequest searchRequest;

    @JsonProperty("elasticsearch")
    private List<String> elasticsearchHosts;

    private String indexName;
}
