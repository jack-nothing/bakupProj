package com.kechun.scheduled;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;


@Component
public class BakUp{

    private static final Logger LOG = LoggerFactory.getLogger(BakUp.class);

//    @Scheduled(cron = "50 59 23 * * ? ")
    @Scheduled(cron = "0/15 * * * * ? ")
    public void bakUp() {
        LOG.info("start:");
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            StringBuffer sbf = new StringBuffer();
            process = runtime.exec(new String[]{
                    "/bin/sh",
                    "-c",
                    sbf.append("ls ")
                            .append("&& ")
                            .append("mkdir /data/BAK/$(date +%Y%m%d) ")
                            .append("&& ")
                            .append("zip -r /data/BAK/$(date +%Y%m%d).zip /data/BAK/Monitor_bakupProj.log ")//project path
                            .append("&& ")
                            .append("> /data/BAK/Monitor_bakupProj.log ")
                            .toString(),
            });
            LOG.info("wait result");
            int status = process.waitFor();
            new ProcessClearStream(process.getInputStream(),"INFO").start();
            new ProcessClearStream(process.getErrorStream(),"ERROR").start();
            Thread.sleep(3000);
            LOG.info("end status:"+status);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }finally {
            if(process!=null)
            process.destroy();
        }
        process = null;
    }


    public boolean checkResult(){

        return false;
    }










}