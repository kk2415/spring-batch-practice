package com.batch.practice.point;

import com.batch.practice.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
