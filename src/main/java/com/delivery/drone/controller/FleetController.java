package com.delivery.drone.controller;

import com.delivery.drone.dto.FleetDto;
import com.delivery.drone.service.FleetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * created by Diluni
 * on 8/27/2022
 */
@Controller
@RequestMapping("fleet")
public class FleetController {
    Logger logger = LoggerFactory.getLogger(FleetController.class);

    @Autowired
    private FleetService fleetService;

    /**
     * REST POST endpoint of adding a fleet
     *
     * @param fleetName
     * @return ResponseEntity
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestParam(required = true) String fleetName) {
        logger.info("Enter the fleet add POST REST API");
        Boolean isSuccess = fleetService.add(fleetName);
        return isSuccess ? new ResponseEntity<>("Fleet has been registered successfully!", HttpStatus.CREATED) : new ResponseEntity<>("Fleet registration is failed!", HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * REST GET endpoint of getting all the fleets
     *
     * @return List
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<FleetDto> getAll() {
        logger.info("Enter the fleet get all GET REST API");
        return fleetService.getAll();
    }
}