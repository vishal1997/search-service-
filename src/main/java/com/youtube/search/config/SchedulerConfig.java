package com.youtube.search.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SchedulerConfig {

    private long initialDelay;
    private long period;
    private int threadPool;
}
