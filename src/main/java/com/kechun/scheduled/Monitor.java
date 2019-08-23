package com.kechun.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Monitor {
    private static final Logger LOG = LoggerFactory.getLogger(Monitor.class);


    /**
     * 30分钟一次，监控
     */
    @Scheduled(cron = "* 0/30 * * * ? ")
    public void monitor() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        String curDay = sdf.format(new Date());
        LOG.info("start:");
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            StringBuffer sbf = new StringBuffer();
            process = runtime.exec(new String[]{
                    "/bin/sh",
                    "-c",
                    sbf.append("ls ").toString()
                    });
        }catch (Exception e){
            e.printStackTrace();
            LOG.info(e.getMessage());
        }

    }
}