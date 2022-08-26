package com.delivery.drone.service;

import com.delivery.drone.dto.MedicationDto;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
public interface MedicationService {
    List<MedicationDto> getAll(String serialNo);

}
