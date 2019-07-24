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
        LOG.info("ohconsole操作备份开启:");
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            LOG.info("start");
            StringBuffer sbf = new StringBuffer();
            process = runtime.exec(new String[]{
                    "/bin/sh",
                    "-c",
                    sbf.append("ls ")
                            .append("&& ")
                            .append("mkdir /data/BAK/$(date +%Y%m%d%H%M) ")
                            .append("&& ")
                            .append("zip -r /data/BAK/$(date +%Y%m%d%H%M).zip /data/BAK/Monitor_bakupProj.log ")
                            .append("&& ")
                            .append("> /data/BAK/Monitor_bakupProj.log ")
                            .toString(),
            });
            int status = process.waitFor();

            new ProcessClearStream(process.getInputStream(),"INFO").start();
            new ProcessClearStream(process.getErrorStream(),"ERROR").start();
            Thread.sleep(3000);
            LOG.info("status:"+status);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }finally {
            if(process!=null)
            process.destroy();
        }
        process = null;
    }

    @Scheduled(cron = "50 59 23 * * ? ")
    public void bakUp_ohtools() {
        LOG.info("bakUp_ohtools");
    }



    //    @Scheduled(cron = "50 59 23 * * ? ")
    @Scheduled(cron = "50 59 23 * * ? ")
    public void bakUp_ohconsole() {
        LOG.info("ohconsole操作备份开启:");
        File wd = new File("/data");

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            process = runtime.exec("/bin/bash", null, wd);
            if (process != null) {
                in = new BufferedReader(new InputStreamReader(process
                        .getInputStream()));
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(process.getOutputStream())), true);
//                out.println("cd /data/BAK && ll && (mkdir $(date +%Y%m%d%H%M)) && ll");
                out.println("mkdir /data/BAK/$(date +%Y%m%d%H%M)");
                out.println("ll");
                out.println("ls");
                out.println("ls");
                String line;
                while ((line = in.readLine()) != null) {
                    LOG.info("bakUp_ohconsole:" + line);
                }
                new ProcessClearStream(process.getInputStream(),"INFO").start();
                new ProcessClearStream(process.getErrorStream(),"ERROR").start();
                int status = process.waitFor();
                LOG.info("status:"+status);
                //process.waitFor();
                process.destroy();
                out.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }finally {
            wd.delete();
        }
    }
}