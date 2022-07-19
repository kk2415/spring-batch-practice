package com.batch.practice.point.wallet;

import com.batch.practice.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
}
