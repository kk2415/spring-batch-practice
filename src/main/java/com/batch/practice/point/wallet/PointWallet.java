package com.batch.practice.point.wallet;

import com.batch.practice.point.IdEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class PointWallet extends IdEntity {

    @Column(unique = true, nullable = false)
    String userId;

    @Column(nullable = false, columnDefinition = "BIGINT")
    Long amount;

    public PointWallet(String userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }

}
