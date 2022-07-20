package com.batch.practice.job.message;

import com.batch.practice.BatchTestSupport;
import com.batch.practice.message.Message;
import com.batch.practice.message.MessageRepository;
import com.batch.practice.point.Point;
import com.batch.practice.point.PointRepository;
import com.batch.practice.point.reservation.PointReservationRepository;
import com.batch.practice.point.wallet.PointWallet;
import com.batch.practice.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class MessageExpiredPointJobConfigurationTest extends BatchTestSupport {

    @Autowired MessageRepository messageRepository;
    @Autowired PointRepository pointRepository;
    @Autowired PointWalletRepository pointWalletRepository;
    @Autowired PointReservationRepository pointReservationRepository;
    @Autowired Job messageExpiredPointJob;

    @Test
    void messageExpiredPointJob() throws Exception {
        // Given
        // 포인트 지갑 생성
        // 오늘(9월 6일) 만료시킨 포인트 적립 내역을 생성 (expiredDate = 어제 9월 5일)
        LocalDate earnedDays = LocalDate.of(2022, 1, 1);
        LocalDate expiredDays = LocalDate.of(2022, 9, 5);
        LocalDate notExpiredDays = LocalDate.of(2026, 12, 31);

        PointWallet pointWallet1 = new PointWallet("user1", 3000L);
        PointWallet pointWallet2 = new PointWallet("user2", 0L);
        pointWalletRepository.save(pointWallet1);
        pointWalletRepository.save(pointWallet2);

        pointRepository.save(new Point(pointWallet2, 1000L, earnedDays, expiredDays, false, true));
        pointRepository.save(new Point(pointWallet2, 1000L, earnedDays, expiredDays, false, true));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, expiredDays, false, true));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, expiredDays, false, true));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, expiredDays, false, true));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, notExpiredDays));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, notExpiredDays));
        pointRepository.save(new Point(pointWallet1, 1000L, earnedDays, notExpiredDays));

        // When
        // messageExpiredPointJob 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2022-09-06")
                .toJobParameters();
        JobExecution jobExecution = launchJob(messageExpiredPointJob, jobParameters);

        // Then
        // 아래와 같은 메세지가 있는지 확인
        // 2022-07-20 기준 3000포인트가 만료되었습니다.
        // 유저1 -> 3000포인트 만료 메시지
        // 유저2 -> 2000포인트 만료 메시지
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<Message> messages = messageRepository.findAll();
        Message user1Message = messages.stream().filter(message -> message.getUserId().equals("user1")).findFirst().orElseGet(null);
        Message user2Message = messages.stream().filter(message -> message.getUserId().equals("user2")).findFirst().orElseGet(null);

        then(messages).hasSize(2);
        then(user1Message).isNotNull();
        then(user2Message).isNotNull();

        then(user1Message.getTitle()).isEqualTo("3000 포인트 만료");
        then(user1Message.getContent()).isEqualTo("2022-09-06 기준 3000 포인트가 만료되었습니다.");
    }
}