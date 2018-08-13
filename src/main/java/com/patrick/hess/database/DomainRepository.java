package com.patrick.hess.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    Domain getBySubdomain(String subdomain);

    Domain getByDomain(String domain);

}
