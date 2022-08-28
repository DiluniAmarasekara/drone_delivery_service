package com.delivery.drone.service;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.dto.MedicationDto;

import java.util.List;
import java.util.Map;

/**
 * created by Diluni
 * on 8/26/2022
 */
public interface DroneService {
    Map<Boolean, String> add(CreateDroneDto createDroneDto);

    Map<Boolean, String> load(String serialNo, List<MedicationDto> medicationDtos);

    List<DroneDto> getAll();

    Map<Boolean, String> getBatteryLevel(String serialNo);

}
