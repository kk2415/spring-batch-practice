package com.batch.practice.job.expire;

import com.batch.practice.BatchTestSupport;
import com.batch.practice.point.Point;
import com.batch.practice.point.PointRepository;
import com.batch.practice.point.wallet.PointWallet;
import com.batch.practice.point.wallet.PointWalletRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class ExpirePointJobConfigurationTest extends BatchTestSupport {

    @Autowired Job expirePointJob;
    @Autowired PointRepository pointRepository;
    @Autowired PointWalletRepository pointWalletRepository;

    @Test
    void expirePointJob() throws Exception {
        // Given
        LocalDate earnedDays = LocalDate.of(2022, 1, 1);
        LocalDate expiredDays = LocalDate.of(2022, 1, 3);

        PointWallet pointWallet = new PointWallet("user123", 6000L);
        pointWalletRepository.save(pointWallet);

        pointRepository.save(new Point(pointWallet, 1000L, earnedDays, expiredDays));
        pointRepository.save(new Point(pointWallet, 1000L, earnedDays, expiredDays));
        pointRepository.save(new Point(pointWallet, 1000L, earnedDays, expiredDays));

        // When
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2022-01-04")
                .toJobParameters();

        JobExecution jobExecution = launchJob(expirePointJob, jobParameters);

        // Then
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Point> points = pointRepository.findAll();
        then(points.stream().filter(Point::isExpired)).hasSize(3);

        PointWallet changedPointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
        then(changedPointWallet).isNotNull();
        then(changedPointWallet.getAmount()).isEqualByComparingTo(3000L);
    }

}