package com.patrick.hess;

import com.patrick.hess.thread.CustomThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;


@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private CustomThreadPoolExecutor threadPoolExecutor;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
