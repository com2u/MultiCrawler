package com.patrick.hess.thread;

import com.patrick.hess.database.PageStats;
import com.patrick.hess.parser.PageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.FutureTask;

/**
 * Created by PS on 15.01.2017.
 */
public class ParserRunner {

    public final Logger LOG;

    private PageParser pageParser;
    private Callback<PageStats> callback;
    private boolean busy;

    public ParserRunner(Integer index, Callback<PageStats> callback) {
        LOG = LoggerFactory.getLogger(String.format("ParserRunner# %d", index));
        this.callback = callback;
        pageParser = new PageParser();
        pageParser.setup();
    }

    public void run(PageStats pageStats) {
        new Thread(() -> {
        if (Objects.nonNull(pageStats)) {
            busy = true;
            PageStats result = null;
            try {
                long start = System.currentTimeMillis();
                LOG.info(String.format("Parsing page: %s", pageStats.getLink()));
                FutureTask<PageStats> futureTask = new FutureTask<>(() -> pageParser.fillPageStats(pageStats));
                futureTask.run();
                result = futureTask.get();
                long end = System.currentTimeMillis();
                long workingTime = end - start;
                LOG.info(String.format("Parsing page: %s done for %d ms", pageStats.getLink(), workingTime));
            } catch (Exception e) {
                LOG.warn(String.format("Parsing page: %s failed", pageStats.getLink()), e);
            } finally {
                busy = false;
                callback.callback(Objects.isNull(result) ? pageStats : result);
            }
        }}).start();
    }

    public void destroy(){
        pageParser.destroy();
    }

    public boolean isBusy() {
        return busy;
    }
}
