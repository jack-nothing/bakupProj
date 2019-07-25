package com.kechun.scheduled;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Component
public class BakUp {


    @Value("${bakUpTargetPath}")
    private String bakUpTargetPath;
    @Value("${xxxx.projPath}")
    private String xxxxProjPath;
    @Value("${xxxx.LogPath}")
    private String xxxxLogPath;

    @Value("${xxx.projPath}")
    private String xxxProjPath;
    @Value("${xxxxx.projPath}")
    private String xxxxxProjPath;
    @Value("${xxx.projPath}")
    private String xxxxxxProjPath;

    @Value("${xxx.LogPath}")
    private String xxxLogPath;

    private static String curDay;


    private static final Logger LOG = LoggerFactory.getLogger(BakUp.class);

//    @Scheduled(cron = "0/30 * * * * ? ")
    @Scheduled(cron = "0 0 6 * * ? ")
    public void bakUp() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        curDay = sdf.format(new Date());
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
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.WEB-INF.zip ").append(xxxProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.catalina.zip ").append(xxxxLogPath + " ")
                            .append("&& ")
                            .append("> ").append(xxxxLogPath)
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.WEB-INF.zip ").append(xxxxxProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.WEB-INF.zip ").append(xxxProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.WEB-INF.zip ").append(xxxProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/xxxx.catalina.zip ").append(xxxLogPath + " ")
                            .append("&& ")
                            .append("> ").append(xxxLogPath)
                            .toString(),
            });
            LOG.info("shell:" + sbf.toString());
            LOG.info("wait result");
            int status = process.waitFor();
            ProcessClearStream processInfoStream = new ProcessClearStream(process.getInputStream(), "INFO",null);
            processInfoStream.start();
            ProcessClearStream processErrorStream = new ProcessClearStream(process.getErrorStream(), "ERROR",null);
            processErrorStream.start();
            Thread.sleep(3000);
            LOG.info("end status:" + status);
            LOG.info("end");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        } finally {
            if (process != null)
                process.destroy();
        }
    }

//    @Scheduled(cron = "0/40 * * * * ? ")
    @Scheduled(cron = "0 0 9 * * ? ")
    public void checkResult() {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        Exception e1=null;
        boolean bakFlag = false;
        try {
            StringBuffer sbf = new StringBuffer();
            process = runtime.exec(new String[]{
                    "/bin/sh",
                    "-c",
                    sbf.
                            append("ls /data/BAK/" + curDay).toString()
            });
            LOG.info("shell:" + sbf.toString());
            LOG.info("wait result");
            int status = process.waitFor();
            ProcessClearStream processInfoStream = new ProcessClearStream(process.getInputStream(), "INFO",curDay);
            processInfoStream.start();
            ProcessClearStream processErrorStream = new ProcessClearStream(process.getErrorStream(), "ERROR",null);
            processErrorStream.start();

            bakFlag = processInfoStream.isBakFlag();

            Thread.sleep(3000);
            LOG.info("end status:" + status);
            LOG.info("end");

        }catch (Exception e){
            e.printStackTrace();
            LOG.info(e.getMessage());
            e1 = e;
        } finally {
            if (process != null)
                process.destroy();
        }

        //TODO send email or phone
        //BUG----run()-->set bakFlag
        if(e1 !=null || bakFlag == false){
            LOG.info("bakUp-fiald!");
        }
        if(bakFlag){
            LOG.info("bakUp-success!");
        }
    }
}