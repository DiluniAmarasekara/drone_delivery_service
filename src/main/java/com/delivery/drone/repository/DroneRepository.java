package com.delivery.drone.repository;

import com.delivery.drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

}
