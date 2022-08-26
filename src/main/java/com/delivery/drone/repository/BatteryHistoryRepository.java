package com.delivery.drone.repository;

import com.delivery.drone.entity.BatteryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * created by Diluni
 * on 8/26/2022
 */
public interface BatteryHistoryRepository extends JpaRepository<BatteryHistory, Long> {
}
