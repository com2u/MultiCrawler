package com.patrick.hess.runner;


import com.patrick.hess.service.DomainService;
import com.patrick.hess.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 2)
public class WebPageApplicationRunner implements ApplicationRunner {

    public static final Logger LOG = LoggerFactory.getLogger(WebPageApplicationRunner.class);

    @Autowired
    private PageService pageService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<String> domainNames = applicationArguments.getNonOptionArgs();

        for (String domainName : domainNames) {
            pageService.getOrCreatePage(domainName);
        }
    }
}
