package com.delivery.drone.service;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.dto.MedicationDto;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
public interface DroneService {
    Boolean add(CreateDroneDto createDroneDto);

    Boolean load(String serialNo, List<MedicationDto> medicationDtos);

    List<DroneDto> getAll();

    String getBatteryLevel(String serialNo);

}
