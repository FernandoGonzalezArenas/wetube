package com.teakter.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

@Bean(name = "transcoderExecutor")
    public Executor transcoderExecutor(){
    ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2); //minimo de hilos a ejecutar
    executor.setMaxPoolSize(4); //maximo de hilos a ejecutar si hay mucha carga de videos
    executor.setQueueCapacity(50); //capasidad de la cola de espera
    executor.setThreadNamePrefix("HLS-Transcoder-");
    executor.initialize();

    return executor;
}

}
