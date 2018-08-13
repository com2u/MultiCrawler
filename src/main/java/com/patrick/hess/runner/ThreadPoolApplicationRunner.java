package com.patrick.hess.runner;

import com.patrick.hess.thread.CustomThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
public class ThreadPoolApplicationRunner implements ApplicationRunner {

    public static final Logger LOG = LoggerFactory.getLogger(ThreadPoolApplicationRunner.class);

    @Autowired
    private CustomThreadPoolExecutor customThreadPoolExecutor;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Integer threadsCount = 1;
        if(applicationArguments.containsOption("threads.count")){
            String threadsCountStr = applicationArguments.getOptionValues("threads.count").get(0);
            if(threadsCountStr != null && threadsCountStr.trim().length() > 0){
                try{
                    threadsCount = Integer.parseInt(threadsCountStr);
                }catch (Exception ignored){
                }
            }
        }

        customThreadPoolExecutor.run(threadsCount);
    }
}