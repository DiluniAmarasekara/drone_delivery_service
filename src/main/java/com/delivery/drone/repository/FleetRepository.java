package com.delivery.drone.repository;

import com.delivery.drone.dto.FleetDto;
import com.delivery.drone.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Repository
public interface FleetRepository extends JpaRepository<Fleet, Long> {
    @Query("SELECT new com.delivery.drone.dto.FleetDto(a.fleetId, a.fleetName, a.noOfDrones) FROM Fleet a")
    List<FleetDto> findAllFleetDtos();
}