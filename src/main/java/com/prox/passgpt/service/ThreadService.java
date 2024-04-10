package com.prox.passgpt.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadService {
    @Getter
    private  static ThreadService instance;
    private ScheduledExecutorService executorService;
    @PostConstruct
    private void init(){
        instance = this;
        executorService = Executors.newScheduledThreadPool(1);
    }

    public static void execute(Runnable runnable){
        instance.executorService.execute(runnable);
    }

    public static void shutdown(){
        instance.executorService.shutdown();
    }

    public static void shutdownNow(){
        instance.executorService.shutdownNow();
    }

    public static boolean isShutdown(){
        return  instance.executorService.isShutdown();
    }

    public static void runAfterDelay(Runnable runnable, long delay){
        instance.executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }


}
