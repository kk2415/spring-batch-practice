package com.batch.practice.job.reservation;

import com.batch.practice.BatchTestSupport;
import com.batch.practice.point.Point;
import com.batch.practice.point.PointRepository;
import com.batch.practice.point.reservation.PointReservation;
import com.batch.practice.point.reservation.PointReservationRepository;
import com.batch.practice.point.wallet.PointWallet;
import com.batch.practice.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;

class ExecutePointReservationJobConfigurationTest extends BatchTestSupport {

    @Autowired PointRepository pointRepository;
    @Autowired PointWalletRepository pointWalletRepository;
    @Autowired PointReservationRepository pointReservationRepository;
    @Autowired Job executePointReservationJob;

    @Test
    void executePointReservationJob() throws Exception {
        // Given
        LocalDate earnedDays = LocalDate.of(2022, 1, 5);

        PointWallet pointWallet = new PointWallet("user123", 6000L);
        pointWalletRepository.save(pointWallet);

        PointReservation pointReservation =
                new PointReservation(pointWallet, 1000L, earnedDays, 10);
        pointReservationRepository.save(pointReservation);

        // When
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2022-01-05")
                .toJobParameters();
        JobExecution jobExecution = launchJob(executePointReservationJob, jobParameters);

        // Then
        // point reservation은 완료처리되야 합니다.
        // 포인트 적립 내역이 생겨함
        // 포인트 지갑의 잔액이 증가해야함.
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<PointReservation> reservations = pointReservationRepository.findAll();
        then(reservations).hasSize(1);
        then(reservations.get(0).isExecuted()).isTrue();

        List<Point> points = pointRepository.findAll();
        then(points).hasSize(1);
        then(points.get(0).getAmount()).isEqualByComparingTo(1000L);
        then(points.get(0).getEarnedDate()).isEqualTo(LocalDate.of(2022, 1, 5));
        then(points.get(0).getExpireDate()).isEqualTo(LocalDate.of(2022, 1, 15));

        List<PointWallet> pointWallets = pointWalletRepository.findAll();
        then(pointWallets).hasSize(1);
        then(pointWallets.get(0).getAmount()).isEqualByComparingTo(7000L);
    }

}