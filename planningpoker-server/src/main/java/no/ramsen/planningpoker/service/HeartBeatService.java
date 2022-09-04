package no.ramsen.planningpoker.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class HeartBeatService {
    private final TaskScheduler heartBeatScheduler;

    public HeartBeatService() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        this.heartBeatScheduler = scheduler;
    }

    public TaskScheduler getHeartBeatScheduler() {
        return heartBeatScheduler;
    }
}
