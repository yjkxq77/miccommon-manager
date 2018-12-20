package com.mc.manager.frame.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author LiuChunfu
 * @date 2018/1/17
 */
@Slf4j
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数为CPU的核心*3
        threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 3);
        // threadPoolTaskExecutor.setCorePoolSize(90);
        //设置核心线程是否允许被关闭
        // threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        //队列的大小
        threadPoolTaskExecutor.setQueueCapacity(90);
        //线程池最大数量
        threadPoolTaskExecutor.setMaxPoolSize(90);
        //临时线程存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(1000);
        //设置线程前缀
        threadPoolTaskExecutor.setThreadNamePrefix("mic-core-bus-");
        //线程池处理不过来的时候交给主线程进行处理
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.debug("【基础线程池】初始化基础线程池成功...");
        return threadPoolTaskExecutor;
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        ThreadFactory factory = (new ThreadFactoryBuilder())
                .setNameFormat("mic-core-schedule-%d")
                .setDaemon(true)
                .build();
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5, factory);
        log.debug("【定时线程池】初始化定时任务线程池成功...");
        return scheduledExecutorService;
    }
}
