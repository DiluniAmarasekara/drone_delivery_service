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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseDto add(@RequestBody(required = true) CreateDroneDto createDroneDto) {
        logger.info("Enter the drone add POST REST API");
        return droneService.add(createDroneDto);
    }

    /**
     * REST POST endpoint of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return ResponseDto
     */
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public ResponseDto load(@RequestParam(required = true) String serialNo, @RequestBody(required = true) List<MedicationDto> medicationDtos) {
        logger.info("Enter the drone load POST REST API");
        return droneService.load(serialNo, medicationDtos);
    }

    /**
     * REST GET endpoint of checking available drones for loading
     *
     * @return List<CreateDroneDto>
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<CreateDroneDto> getAll() {
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
