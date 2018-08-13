package com.patrick.hess.service;

import com.patrick.hess.database.Domain;
import com.patrick.hess.database.DomainRepository;
import com.patrick.hess.util.DomainUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Service
@Transactional
public class DomainService {

    public static final Logger LOG = LoggerFactory.getLogger(DomainService.class);

    @Autowired
    private DomainRepository domainRepository;

    public Domain getBySubdomain(String domain) {
        return domainRepository.getBySubdomain(domain);
    }

    public Domain getOrCreateDomain(String subdomainName) {

        try {
            Domain domain = getBySubdomain(subdomainName);
            if(Objects.isNull(domain)) {
                InetAddress domainAddress = InetAddress.getByName(subdomainName);
                String domainName = DomainUtils.getBaseDomainName(subdomainName);
                domain = new Domain();
                domain.setDomain(domainName)
                        .setSubdomain(subdomainName)
                        .setIp(domainAddress.getHostAddress())
                        .setCreatedDate(Instant.now());
                domainRepository.save(domain);
                LOG.debug(String.format("Added new domain: %s", subdomainName));
            }
            return domain;
        } catch (Exception e) {
            LOG.warn(String.format("InetAddress address not found by subdomainName: %s", subdomainName));
            return null;
        }
    }


}
