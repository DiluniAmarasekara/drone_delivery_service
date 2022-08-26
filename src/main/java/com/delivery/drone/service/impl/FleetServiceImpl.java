package com.delivery.drone.service.impl;

import com.delivery.drone.dto.ResponseDto;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.repository.FleetRepository;
import com.delivery.drone.service.FleetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Transactional
    @Override
    public Boolean add(String fleetName) {
        Fleet fleet = new Fleet(fleetName, 0);
        try {
            fleetRepository.save(fleet);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while adding the fleet");
        }
        return Boolean.TRUE;
//        return new ResponseDto(HttpStatus.CREATED.value(), "Fleet has been registered successfully!");
    }

    @Override
    public List<Fleet> getAll() {
        try {
            return fleetRepository.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while getting all the fleets");
        }
    }

}
