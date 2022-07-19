package com.batch.practice.message;

import com.batch.practice.point.IdEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

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

}
