package com.delivery.drone.controller;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
     * REST POST endpoint of registering a drone
     *
     * @param createDroneDto
     * @return ResponseEntity
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> add(@Validated @RequestBody(required = true) CreateDroneDto createDroneDto) {
        logger.info("Enter the drone add POST REST API");
        Boolean isSuccess = droneService.add(createDroneDto);
        return isSuccess ? new ResponseEntity<>("Drone has been registered successfully!", HttpStatus.CREATED) : new ResponseEntity<>("Drone registration is failed!", HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * REST POST endpoint of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return ResponseEntity
     */
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public ResponseEntity<String> load(@Validated @RequestBody(required = true) List<MedicationDto> medicationDtos, @RequestParam(required = true) String serialNo) {
        logger.info("Enter the drone load POST REST API");
        Boolean isSuccess = droneService.load(serialNo, medicationDtos);
        return isSuccess ? new ResponseEntity<>("Drone loading is successful!", HttpStatus.CREATED) : new ResponseEntity<>("Drone loading is failed!", HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * REST GET endpoint of checking available drones for loading
     *
     * @return List<DroneDto>
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<DroneDto> getAll() {
        logger.info("Enter the drone get all GET REST API");
        return droneService.getAll();
    }

    /**
     * REST GET endpoint of check drone battery level for a given drone
     *
     * @param serialNo
     * @return String
     */
    @RequestMapping(value = "/get/batteryLevel", method = RequestMethod.GET)
    public String getBatteryLevel(@RequestParam(required = true) String serialNo) {
        logger.info("Enter the drone get battery level GET REST API");
        return droneService.getBatteryLevel(serialNo);
    }

}
