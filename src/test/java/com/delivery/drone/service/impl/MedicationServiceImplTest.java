package com.delivery.drone.service.impl;

import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * created by Diluni
 * on 8/26/2022
 */
@ExtendWith(SpringExtension.class)
class MedicationServiceImplTest {
    @InjectMocks
    private MedicationServiceImpl medicationService;

    @Mock
    private MedicationRepository medicationRepository;

    /**
     * Success scenario of get all loaded medications of the drone
     */
    @Test
    void getAll() {
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 40.0, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        when(medicationRepository.findAllMedicationDtos("DC-13245")).thenReturn(medicationDtos);

        List<MedicationDto> actualResult = medicationService.getAll("DC-13245");
        assertEquals(1, actualResult.size());
    }

    /**
     * Exception scenario of get all loaded medications of the drone
     */
    @Test
    void getAllException() {
        when(medicationRepository.findAllMedicationDtos(any())).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            medicationService.getAll("DC-13245");
        });
        assertEquals("Exception is occurred while getting loaded medications of drone", exception.getMessage());
    }
}