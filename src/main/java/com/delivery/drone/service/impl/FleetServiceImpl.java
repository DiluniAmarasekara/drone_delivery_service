package com.delivery.drone.service.impl;

import com.delivery.drone.dto.FleetDto;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.repository.FleetRepository;
import com.delivery.drone.service.FleetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * created by Diluni
 * on 8/27/2022
 */
@Service
public class FleetServiceImpl implements FleetService {
    Logger logger = LoggerFactory.getLogger(FleetServiceImpl.class);

    @Autowired
    private FleetRepository fleetRepository;

    /**
     * Implementation of add fleet
     *
     * @param fleetName
     * @return Boolean
     */
    @Transactional
    @Override
    public Boolean add(String fleetName) {
        logger.info("Enter the add fleet of drone service implementation");
        Fleet fleet = new Fleet(fleetName, 0);
        try {
            fleetRepository.save(fleet);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while adding the fleet");
        }
        logger.info("Exit the add fleet of drone service implementation");
        return Boolean.TRUE;
    }

    /**
     * Implementation of get all fleets
     *
     * @return List
     */
    @Override
    public List<FleetDto> getAll() {
        logger.info("Enter the get all fleets service implementation");
        List<FleetDto> fleetDtos;
        try {
            fleetDtos = fleetRepository.findAllFleetDtos();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while getting all the fleets");
        }
        logger.info("Exit the get all fleets service implementation");
        return fleetDtos;
    }
}