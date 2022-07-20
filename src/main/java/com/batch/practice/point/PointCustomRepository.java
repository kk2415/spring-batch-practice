package com.batch.practice.point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PointCustomRepository {

    Page<ExpiredPointSummary> sumByExpiredDate(LocalDate alarmCriteriaDate, Pageable pageable);
    Page<ExpiredPointSummary> sumByBeforeCriteriaDate(LocalDate alarmCriteriaDate, Pageable pageable);
    Page<Point> findPointToExpire(LocalDate today, Pageable pageable);

}
