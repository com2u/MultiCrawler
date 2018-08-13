package com.patrick.hess.database;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageStats, Long> {

    PageStats getByUrl(String url);

    List<PageStats> getByVisitedDateNullAndIdNotIn(List<Long> excludedIds, Pageable pageable);
    List<PageStats> getByVisitedDateNullAndIdNotIn(List<Long> excludedIds);
    List<PageStats> getByVisitedDateNull(Pageable pageable);
    List<PageStats> getByVisitedDateNull();
    List<PageStats> getByVisitedDateNotNull(Pageable pageable);

}
