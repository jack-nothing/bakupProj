package com.kechun.scheduled;


import com.kechun.util.EmailSender;
import com.kechun.util.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
    @Value("${ohconsole.projPath}")
    private String ohconsoleProjPath;
    @Value("${ohconsole.LogPath}")
    private String ohconsoleLogPath;

    @Value("${mcs.projPath}")
    private String mcsProjPath;
    @Value("${ohtools.projPath}")
    private String ohtoolsProjPath;
    @Value("${mqtt.projPath}")
    private String mqttProjPath;

    @Value("${mcs.LogPath}")
    private String mcsLogPath;


    @Value("${fromUser}")
    private String fromUser;
    @Value("${host}")
    private String host;
    @Value("${emailAccount}")
    private String emailAccount;
    @Value("${emailPassword}")
    private String emailPassword;
    @Value("${emailEnCode}")
    private String emailEnCode;
    @Value("${emailProtocol}")
    private String emailProtocol;
    @Value("${port}")
    private String port;

    private static String curDay;

    private JavaMailSenderImpl javaMailSender = null;
    static final ThreadPoolManager tpm = new ThreadPoolManager(8);

    private static final Logger LOG = LoggerFactory.getLogger(BakUp.class);

    private JavaMailSenderImpl getInstall(){
        if (javaMailSender ==null){
            javaMailSender = initJavaMailSenderImpl();
        }
        return javaMailSender;
    }

    @Scheduled(cron = "0/30 * * * * ? ")
//    @Scheduled(cron = "0 0 6 * * ? ")
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
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/ohconsole.WEB-INF.zip ").append(ohconsoleProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/ohconsole.catalina.zip ").append(ohconsoleLogPath + " ")
                            .append("&& ")
                            .append("> ").append(ohconsoleLogPath)
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/mcs.WEB-INF.zip ").append(mcsProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/ohtools.WEB-INF.zip ").append(ohtoolsProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/mqtt.WEB-INF.zip ").append(mqttProjPath + " ")
                            .append("&& ")
                            .append("zip -q -r ").append(bakUpTargetPath).append("$(date +%Y%m%d)/mcs_ohtools_mqtt.catalina.zip ").append(mcsLogPath + " ")
                            .append("&& ")
                            .append("> ").append(mcsLogPath)
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

    @Scheduled(cron = "0/40 * * * * ? ")
//    @Scheduled(cron = "0 0 9 * * ? ")
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
        getInstall();
        tpm.addExecuteTask(new EmailSender(javaMailSender, "cc@163.com", "857812506@qq.com", fromUser, "BAK-END", "BAK:"+curDay));


        //TODO send email or phone
        //BUG----run()-->set bakFlag
        if(e1 !=null || bakFlag == false){
//            LOG.info("bakUp-fiald!");
        }
        if(bakFlag){
//            LOG.info("bakUp-success!");
        }
    }




    private JavaMailSenderImpl initJavaMailSenderImpl(){
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(emailAccount);
        javaMailSender.setPassword(emailPassword);
        javaMailSender.setDefaultEncoding(emailEnCode);
        javaMailSender.setProtocol(emailProtocol);
        javaMailSender.setPort(Integer.parseInt(port));
        return javaMailSender;
    }

}