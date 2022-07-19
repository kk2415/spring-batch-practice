package com.batch.practice.point.reservation;

import com.batch.practice.point.IdEntity;
import com.batch.practice.point.wallet.PointWallet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PointReservation extends IdEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    PointWallet pointWallet;

    @Column(nullable = false, columnDefinition = "BIGINT")
    Long amount;

    @Column(nullable = false)
    LocalDate earnedDate; //적립할 날짜

    @Column(nullable = false)
    int availableDays;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    boolean executed;

    public PointReservation(PointWallet pointWallet, Long amount, LocalDate earnedDate, int availableDays) {
        this.pointWallet = pointWallet;
        this.amount = amount;
        this.earnedDate = earnedDate;
        this.availableDays = availableDays;
        this.executed = false;
    }

    public void execute() {
        this.executed = true;
    }

    public LocalDate getExpireDate() {
        return this.earnedDate.plusDays(this.availableDays);
    }

}
