package com.batch.practice.point.reservation;

import com.batch.practice.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointReservationRepository extends JpaRepository<PointReservation, Long> {
}
