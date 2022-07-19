package com.batch.practice.point.wallet;

import com.batch.practice.point.IdEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PointWallet extends IdEntity {

    @Column(unique = true, nullable = false)
    String userId;

    @Column(nullable = false, columnDefinition = "BIGINT")
    Long amount;

}
