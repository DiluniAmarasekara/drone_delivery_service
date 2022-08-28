package com.delivery.drone.service.impl;

import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.repository.MedicationRepository;
import com.delivery.drone.service.MedicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Service
public class MedicationServiceImpl implements MedicationService {
    Logger logger = LoggerFactory.getLogger(MedicationServiceImpl.class);

    @Autowired
    private MedicationRepository medicationRepository;

    /**
     * Implementation of checking loaded medication items for a given drone
     *
     * @param serialNo
     * @return List
     */
    @Override
    public List<MedicationDto> getAll(String serialNo) {
        List<MedicationDto> medicationDtos;
        try {
            medicationDtos = medicationRepository.findAllMedicationDtos(serialNo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while getting loaded medications of drone");
        }
        return medicationDtos;
    }

}
