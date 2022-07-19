package com.batch.practice.point;

import com.batch.practice.point.wallet.PointWallet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Point extends IdEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    PointWallet pointWallet;

    @Column(nullable = false, columnDefinition = "BIGINT")
    Long amount;

    @Column(nullable = false)
    LocalDate earnedDate;

    @Column(nullable = false)
    LocalDate expireDate;

    @Column(name = "is_used", nullable = false, columnDefinition = "TINYINT", length = 1)
    boolean used;

    @Column(name = "is_expired", nullable = false, columnDefinition = "TINYINT", length = 1)
    boolean expired;

    public Point(PointWallet pointWallet, Long amount, LocalDate earnedDate, LocalDate expireDate) {
        this.pointWallet = pointWallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.expireDate = expireDate;
        this.used = false;
        this.expired = false;
    }

    public void expired() {
        if (!this.used) {
            this.expired = true;
        }
    }

}
