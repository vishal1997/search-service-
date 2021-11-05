package com.youtube.search.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HttpConnectionConfig {

    @NotNull
    private Long httpClientTimeToLiveInMs;
    @NotNull
    private Integer httpClientMaxTotal;
    @NotNull
    private Integer httpClientMaxTotalPerRoute;
    @NotNull
    private Integer httpClientConnectTimeoutMs;
    @NotNull
    private Integer httpClientSocketTimeoutMs;
}
