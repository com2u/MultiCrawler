package com.patrick.hess.thread;

import com.patrick.hess.database.PageStats;
import com.patrick.hess.service.PageService;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomThreadPoolExecutor {

    public static final Logger LOG = LoggerFactory.getLogger(CustomThreadPoolExecutor.class);
    public static final int DEFAULT_THREADS_COUNT = 1;

    @Autowired
    private PageService pageService;

    @Value("${multicrawler.pollexecutor.delay}")
    private Long delay;

    private List<Long> processingPagesIds = new LinkedList<>();
    private ExecutorState state = ExecutorState.RUNNING;
    private List<ParserRunner> runners = new LinkedList<>();
    private boolean hasFreeRunners = true;

    @PreDestroy
    public void exit() {
        terminate();
        LOG.info("Exit gracefully");
    }

    private void initRunners(Integer threadsCount) {
        threadsCount = Objects.nonNull(threadsCount) ? threadsCount : DEFAULT_THREADS_COUNT;
        List<ParserRunner> runnerss = new LinkedList<>();
        for (int i = 1; i <= threadsCount; i++) {
            runnerss.add(new ParserRunner(i, this::processParsedPage));
        }
        runners = Collections.unmodifiableList(runnerss);
        LOG.debug(String.format("ThreadPoolExecutor with %d runners", runners.size()));
    }

    public synchronized void processParsedPage(PageStats pageStats) {
        pageStats.setVisitedDate(Instant.now());
        pageService.save(pageStats);
        processingPagesIds.remove(pageStats.getId());

        if(Objects.nonNull(pageStats.getListOfLinks())) {
            String[] newLinks = pageStats.getListOfLinks().split(",");
            for (String newLink : newLinks) {
                pageService.getOrCreatePage(newLink);
            }
        }
        hasFreeRunners = true;
    }

    public void processPages() {
        List<ParserRunner> freeRunners = runners.stream().filter(parserRunner -> !parserRunner.isBusy())
                .collect(Collectors.toList());
        List<PageStats> pagesForParsing = pageService.getPagesForParsing(processingPagesIds, freeRunners.size());
        Iterator<PageStats> iterator = pagesForParsing.iterator();
        freeRunners.stream().forEach(parserRunner -> {
            if (iterator.hasNext()) {
                PageStats pageStats = iterator.next();
                parserRunner.run(pageStats);
                processingPagesIds.add(pageStats.getId());
            }
        });

        hasFreeRunners = false;
    }

    public void run(Integer threadsCount) {
        new Thread(() -> {
            LOG.debug("ThreadPoolExecutor started");

            initRunners(threadsCount);

            while (!ExecutorState.TERMINATING.equals(state)) {

                if (hasFreeRunners) {
                    processPages();
                }

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    terminate();
                }
            }

            runners.stream().forEach(ParserRunner::destroy);

            LOG.debug("ThreadPoolExecutor terminated");
        }).start();
    }

    public void terminate() {
        this.state = ExecutorState.TERMINATING;
    }
}
