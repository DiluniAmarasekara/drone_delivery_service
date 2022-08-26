package com.delivery.drone.repository;

import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.util.EnumUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    @Query("SELECT DISTINCT new com.delivery.drone.dto.DroneDto(b.serialNo, b.model, b.availableWeight, b.batteryCapacity, b.state, a.fleetId) FROM Fleet a LEFT JOIN Drone b ON a.fleetId=b.fleet.fleetId WHERE a.noOfDrones<10 AND b.batteryCapacity>=25 AND b.availableWeight>0")
    List<DroneDto> findAllAvailableDroneDtos();

    List<Drone> findByStateNotIn(List<EnumUtil.State> asList);

    Optional<Drone> findBySerialNo(String serialNo);

}
