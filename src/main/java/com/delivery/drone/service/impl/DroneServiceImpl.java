package com.delivery.drone.service.impl;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ResponseDto;
import com.delivery.drone.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Service
public class DroneServiceImpl implements DroneService {
    Logger logger = LoggerFactory.getLogger(DroneServiceImpl.class);

    /**
     * Implementation of registering a drone
     *
     * @param droneDto
     * @return ResponseDto
     */
    @Override
    public ResponseDto registerDrone(CreateDroneDto droneDto) {
        return null;
    }

    /**
     * Implementation of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return ResponseDto
     */
    @Override
    public ResponseDto loadDrone(String serialNo, List<MedicationDto> medicationDtos) {
        return null;
    }

    /**
     * Implementation of checking loaded medication items for a given drone
     *
     * @param serialNo
     * @return List<MedicationDto>
     */
    @Override
    public List<MedicationDto> getDrone(String serialNo) {
        return null;
    }

    /**
     * Implementation of checking available drones for loading
     *
     * @return List<CreateDroneDto>
     */
    @Override
    public List<CreateDroneDto> getAvailableDrones() {
        return null;
    }

    /**
     * Implementation of check drone battery level for a given drone
     *
     * @param serialNo
     * @return String
     */
    @Override
    public String getBatteryLevelOfDrone(String serialNo) {
        return null;
    }

}
