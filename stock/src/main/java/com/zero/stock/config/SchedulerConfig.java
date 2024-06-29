package com.zero.stock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        int n = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processors: " + n);

        threadPool.setPoolSize(n);
        threadPool.initialize();
        threadPool.setThreadNamePrefix("stock-scheduler-thread-");
        taskRegistrar.setTaskScheduler(threadPool);
    }
}
