package com.patrick.hess.runner;


import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class DatabaseApplicationRunner implements ApplicationRunner {

    public static final Logger LOG = LoggerFactory.getLogger(DatabaseApplicationRunner.class);

    @Autowired
    private Flyway flyway;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        boolean create = applicationArguments.containsOption("create");
        if(create) {
            LOG.info("Creating DB tables");
            flyway.clean();
            flyway.migrate();
            LOG.info("DB tables have been created.");
        }
    }
}
