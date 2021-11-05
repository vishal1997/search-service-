package com.youtube.search.module;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.dao.YoutubeDataDao;
import com.youtube.search.dao.impl.YoutubeDataDaoImpl;
import com.youtube.search.manager.YoutubeDataManager;
import com.youtube.search.manager.impl.YoutubeDataManagerImpl;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SearchModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(YoutubeDataManager.class).to(YoutubeDataManagerImpl.class);
        bind(YoutubeDataDao.class).to(YoutubeDataDaoImpl.class);
    }

    @Provides
    @Singleton
    @Named("youtubeHttpClient")
    public CloseableHttpClient getCloseableHttpClient(SearchAppConfig config) {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                config.getYoutubeHttpConfig().getHttpClientTimeToLiveInMs(),
                TimeUnit.MILLISECONDS);

        connectionManager.setMaxTotal(config.getYoutubeHttpConfig().getHttpClientMaxTotal());
        connectionManager.setDefaultMaxPerRoute(config.getYoutubeHttpConfig().getHttpClientMaxTotalPerRoute());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                connectionManager.close();
            }
        });

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(config.getYoutubeHttpConfig().getHttpClientConnectTimeoutMs());
        requestConfigBuilder.setSocketTimeout(config.getYoutubeHttpConfig().getHttpClientSocketTimeoutMs());

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
        httpClientBuilder.setConnectionManager(connectionManager);

        CloseableHttpClient httpClient = httpClientBuilder.build();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return httpClient;
    }

    @Provides
    @Singleton
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new GuavaModule());
        return objectMapper;
    }

    @Provides
    @Singleton
    RestHighLevelClient provideElasticSearchClient(SearchAppConfig config) {
        HttpHost[] httpHosts = config.getElasticsearchHosts().stream()
                .map(HttpHost::create)
                .toArray(HttpHost[]::new);
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }
}
