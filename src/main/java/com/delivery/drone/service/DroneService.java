package com.delivery.drone.service;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ResponseDto;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
public interface DroneService {
    ResponseDto registerDrone(CreateDroneDto droneDto);

    List<MedicationDto> getDrone(String serialNo);

    ResponseDto loadDrone(String serialNo, List<MedicationDto> medicationDtos);

    List<CreateDroneDto> getAvailableDrones();

    String getBatteryLevelOfDrone(String serialNo);

}
