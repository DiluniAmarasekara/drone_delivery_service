package com.delivery.drone.controller;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ValidList;
import com.delivery.drone.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<String> add(@Valid @RequestBody(required = true) CreateDroneDto createDroneDto) {
        logger.info("Enter the drone add POST REST API");
        Map<Boolean, String> output = droneService.add(createDroneDto);
        return output.containsKey(true) ? new ResponseEntity<>(output.get(true), HttpStatus.CREATED) : new ResponseEntity<>(output.get(false), HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * REST POST endpoint of loading a drone with medication items
     *
     * @param medicationDtos
     * @param serialNo
     * @return ResponseEntity
     */
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public ResponseEntity<String> load(@Valid @RequestBody ValidList<MedicationDto> medicationDtos, @RequestParam @Size(max = 100, message = "Serial Number should be max 100 characters!") String serialNo) {
        logger.info("Enter the drone load POST REST API");
        Map<Boolean, String> output = droneService.load(serialNo, medicationDtos.getList());
        return output.containsKey(true) ? new ResponseEntity<>(output.get(true), HttpStatus.CREATED) : new ResponseEntity<>(output.get(false), HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * REST GET endpoint of checking available drones for loading
     *
     * @return List
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
     * @return ResponseEntity
     */
    @RequestMapping(value = "/get/batteryLevel", method = RequestMethod.GET)
    public ResponseEntity<String> getBatteryLevel(@RequestParam @Size(max = 100, message = "Serial Number should be max 100 characters!") String serialNo) {
        logger.info("Enter the drone get battery level GET REST API");
        Map<Boolean, String> output = droneService.getBatteryLevel(serialNo);
        return output.containsKey(true) ? new ResponseEntity<>(output.get(true), HttpStatus.OK) : new ResponseEntity<>(output.get(false), HttpStatus.NOT_FOUND);
    }

    /**
     * REST PUT endpoint of update drone status LOADED->DELIVERING->DELIVERED->RETURNING->IDLE
     *
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/update/state", method = RequestMethod.PUT)
    public ResponseEntity<String> updateState(@RequestParam @Size(max = 100, message = "Serial Number should be max 100 characters!") String serialNo) {
        logger.info("Enter the update state POST REST API");
        Map<Boolean, String> output = droneService.updateState(serialNo);
        return output.containsKey(true) ? new ResponseEntity<>(output.get(true), HttpStatus.CREATED) : new ResponseEntity<>(output.get(false), HttpStatus.EXPECTATION_FAILED);
    }

}