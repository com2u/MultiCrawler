package com.patrick.hess.service;

import com.patrick.hess.database.Domain;
import com.patrick.hess.database.PageStats;
import com.patrick.hess.database.PageRepository;
import com.patrick.hess.util.DomainUtils;
import com.patrick.hess.util.PageUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PageService {

    public static final Logger LOG = LoggerFactory.getLogger(PageService.class);

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private DomainService domainService;

    public List<PageStats> getPagesForParsing(List<Long> excludedIds, Integer count){
        Pageable limit = new PageRequest(0, count);
        return  excludedIds.size() > 0 ?
                pageRepository.getByVisitedDateNullAndIdNotIn(excludedIds, limit) :
                pageRepository.getByVisitedDateNull(limit);
    }

    public PageStats save(PageStats pageStats){
        return pageRepository.save(pageStats);
    }

    public PageStats getByUrl(String url) {
        return pageRepository.getByUrl(url);
    }

    public PageStats getOrCreatePage(String link) {
        if (Objects.isNull(link)) return null;
        String url = PageUtils.trimQueryParams(link);

        PageStats pageStats = getByUrl(url);
        if (Objects.isNull(pageStats)) {

            String domainName = DomainUtils.getDomainName(link);
            if (Objects.isNull(domainName)) return null;

            Domain domain = domainService.getOrCreateDomain(domainName);
            if (Objects.isNull(domain)) return null;

            pageStats = new PageStats();
            pageStats.setCreatedDate(Instant.now())
                    .setDomain(domain)
                    .setLink(link)
                    .setUrl(url);

            pageRepository.save(pageStats);
            LOG.debug(String.format("Added new page: %s", url));
        }

        return pageStats;
    }

}
