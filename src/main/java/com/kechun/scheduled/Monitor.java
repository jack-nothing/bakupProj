package com.kechun.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Monitor {
    private static final Logger LOG = LoggerFactory.getLogger(Monitor.class);



    @Scheduled(cron = "50 59 23 * * ? ")
    public void monitor() {
        LOG.info("bakUp_ohtools");
    }
}