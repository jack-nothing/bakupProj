package com.kechun.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessClearStream extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessClearStream.class);
    private InputStream inputStream;
    private String type;
    private boolean bakFlag;
    private String cruDay;

    ProcessClearStream(InputStream inputStream, String type, String cruDay) {
        this.inputStream = inputStream;
        this.type = type;
        this.cruDay = cruDay;
    }

    public void run() {
        BufferedReader br = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                LOG.info(type + ":" + line);
                if (cruDay != null) {
                    if ("xxxx.WEB-INF.zip".equals(line) || line.indexOf("xxxx.WEB-INF.zip") != -1){
                        LOG.info("CHECK:");
                        LOG.info(type + ":" + line);
                        bakFlag = true;
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isBakFlag() {
        return bakFlag;
    }

    public void setBakFlag(boolean bakFlag) {
        this.bakFlag = bakFlag;
    }
}