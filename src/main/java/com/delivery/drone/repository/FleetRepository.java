package com.delivery.drone.repository;

import com.delivery.drone.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Repository
public interface FleetRepository extends JpaRepository<Fleet, Long> {
}
