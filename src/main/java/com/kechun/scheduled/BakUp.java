package com.kechun.scheduled;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;


@Component
public class BakUp{


    @Value("${bakUpTargetPath}")
    private String bakUpTargetPath;
    @Value("${projPath}")
    private String projPath;
    @Value("${LogPath}")
    private String LogPath;

    private static final Logger LOG = LoggerFactory.getLogger(BakUp.class);

    @Scheduled(cron = "0/30 * * * * ? ")
    public void bakUp() {
        LOG.info("start:");
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            StringBuffer sbf = new StringBuffer();
            process = runtime.exec(new String[]{
                    "/bin/sh",
                    "-c ",
                    sbf.append("ls ")
                            .append(" && ")
                            .append("mkdir /data/BAK/$(date +%Y%m%d) ")
                            .append(" && ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/pro_bak.zip ").append(projPath)
                            .append(" && ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/catalina.zip ").append(LogPath)
                            .append(" && ")
                            .append(" > ").append(LogPath)
                            .toString(),
            });
            LOG.info("shell:"+sbf.toString());
            LOG.info("wait result");
            int status = process.waitFor();
            new ProcessClearStream(process.getInputStream(),"INFO").start();
            new ProcessClearStream(process.getErrorStream(),"ERROR").start();
            Thread.sleep(10000);
//            LOG.info("end status:"+status);
            LOG.info("end");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }finally {
            if(process!=null)
            process.destroy();
        }
    }


    public boolean checkResult(){

        return false;
    }










}