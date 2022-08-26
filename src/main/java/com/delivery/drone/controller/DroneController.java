package com.delivery.drone.controller;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ResponseDto;
import com.delivery.drone.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@RestController
@RequestMapping("drone")
public class DroneController {
    Logger logger = LoggerFactory.getLogger(DroneController.class);

    @Autowired
    private DroneService droneService;

    /**
     * REST endpoint of registering a drone
     *
     * @param createDroneDto
     * @return ResponseDto
     */
    @RequestMapping(value = "/registerDrone", method = RequestMethod.POST)
    public ResponseDto registerDrone(@RequestBody(required = true) CreateDroneDto createDroneDto) {
        logger.info("Enter the register drone POST REST API");
        return droneService.registerDrone(createDroneDto);
    }

    /**
     * REST POST endpoint of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return ResponseDto
     */
    @RequestMapping(value = "/loadDrone", method = RequestMethod.POST)
    public ResponseDto loadDrone(@RequestParam(required = true) String serialNo, @RequestBody(required = true) List<MedicationDto> medicationDtos) {
        logger.info("Enter the load the drone POST REST API");
        return droneService.loadDrone(serialNo, medicationDtos);
    }

    /**
     * REST GET endpoint of checking loaded medication items for a given drone
     *
     * @param serialNo
     * @return List<MedicationDto>
     */
    @RequestMapping(value = "/getDrone", method = RequestMethod.GET)
    public List<MedicationDto> getDrone(@RequestParam(required = true) String serialNo) {
        logger.info("Enter the get drone GET REST API");
        return droneService.getDrone(serialNo);
    }

    /**
     * REST GET endpoint of checking available drones for loading
     *
     * @return List<CreateDroneDto>
     */
    @RequestMapping(value = "/getAvailableDrones", method = RequestMethod.GET)
    public List<CreateDroneDto> getAvailableDrones() {
        logger.info("Enter the get available drones GET REST API");
        return droneService.getAvailableDrones();
    }

    /**
     * REST GET endpoint of check drone battery level for a given drone
     *
     * @param serialNo
     * @return String
     */
    @RequestMapping(value = "/getBatteryLevelOfDrone", method = RequestMethod.GET)
    public String getBatteryLevelOfDrone(@RequestParam(required = true) String serialNo) {
        logger.info("Enter the get battery level of drone GET REST API");
        return droneService.getBatteryLevelOfDrone(serialNo);
    }

}
