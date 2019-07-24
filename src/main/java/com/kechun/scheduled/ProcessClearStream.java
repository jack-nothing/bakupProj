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
    ProcessClearStream(InputStream inputStream, String type) {
        this.inputStream = inputStream;
        this.type = type;
    }

    public void run() {
        BufferedReader br = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream);
             br = new BufferedReader(inputStreamReader);
            // 打印信息
            String line = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(type + ">" + line);
                LOG.info(type+":"+line);

            }
            // 不打印信息
//           while (br.readLine() != null);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}