package com.youtube.search;

import com.codahale.metrics.health.HealthCheck;

public class SearchAppHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
