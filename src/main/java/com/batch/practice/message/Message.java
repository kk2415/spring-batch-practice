package com.batch.practice.message;

import com.batch.practice.point.IdEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Message extends IdEntity {

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "text")
    String content;

    public Message(String userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public static Message expiredPointMessageInstance(String userId, LocalDate expiredDate, Long expiredAmount) {
        return new Message(
            userId,
            String.format("%s 포인트 만료", expiredAmount.toString()),
            String.format(
                "%s 기준 %s 포인트가 만료되었습니다.", expiredDate.format(DateTimeFormatter.ISO_DATE) ,expiredAmount
            )
        );
    }

    public static Message expireSoonPointMessageInstance(String userId, LocalDate expiredDate, Long expiredAmount) {
        return new Message(
                userId,
                String.format("%s 포인트 예정", expiredAmount.toString()),
                String.format(
                        "%s 까지 %s 포인트가 만료 예정입니다.", expiredDate.format(DateTimeFormatter.ISO_DATE) ,expiredAmount
                )
        );
    }

}
