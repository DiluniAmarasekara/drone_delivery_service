package com.delivery.drone.controller;

import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.service.MedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@RestController
@RequestMapping("medication")
public class MedicationController {
    Logger logger = LoggerFactory.getLogger(MedicationController.class);

    @Autowired
    private MedicationService medicationService;

    /**
     * REST GET endpoint of checking loaded medication items for a given drone
     *
     * @param serialNo
     * @return List<MedicationDto>
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<MedicationDto> getAll(@RequestParam @Size(max = 100, message = "Serial Number should be max 100 characters!") String serialNo) {
        logger.info("Enter the get all GET REST API");
        return medicationService.getAll(serialNo);
    }

}