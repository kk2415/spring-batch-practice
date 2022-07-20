package com.batch.practice.point;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ExpiredPointSummary {
    String userId;
    Long amount;

    @QueryProjection
    public ExpiredPointSummary(String userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
