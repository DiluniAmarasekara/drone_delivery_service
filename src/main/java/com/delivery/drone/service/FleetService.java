package com.delivery.drone.service;

import com.delivery.drone.dto.FleetDto;

import java.util.List;

/**
 * created by Diluni
 * on 8/27/2022
 */
public interface FleetService {
    Boolean add(String fleetName);

    List<FleetDto> getAll();
}