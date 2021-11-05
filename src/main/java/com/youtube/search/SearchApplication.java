package com.youtube.search;

import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.youtube.search.config.SearchAppConfig;
import com.youtube.search.managd.SearchScheduler;
import com.youtube.search.manager.impl.YoutubeSearchManager;
import com.youtube.search.module.SearchModule;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchApplication extends Application<SearchAppConfig> {

    private GuiceBundle<SearchAppConfig> guiceBundle;

    @Override
    public void run(SearchAppConfig searchAppConfig, Environment environment) throws Exception {

        environment.jersey().register(new JsonProcessingExceptionMapper(true));

        environment.healthChecks().register("search-app",
                guiceBundle.getInjector().getInstance(SearchAppHealthCheck.class));

        SearchScheduler searchScheduler = guiceBundle.getInjector().getInstance(SearchScheduler.class);
        try {
            searchScheduler.start();
            log.info("Started search scheduler");
        } catch (Exception e) {
            log.error("Error starting search scheduler {}", e);
        }
    }

    @Override
    public void initialize(Bootstrap<SearchAppConfig> bootstrap) {
        guiceBundle = new GuiceBundle.Builder<SearchAppConfig>()
                .addModule(new SearchModule())
                .setConfigClass(SearchAppConfig.class)
                .enableAutoConfig("com.youtube.search.resource")
                .build(Stage.DEVELOPMENT);

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new SwaggerBundle<SearchAppConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SearchAppConfig appConfig) {
                return appConfig.getSwaggerBundleConfiguration();
            }
        });

        super.initialize(bootstrap);
    }

    public static void main(String[] args) throws Exception {
        new SearchApplication().run(args);
    }
}
