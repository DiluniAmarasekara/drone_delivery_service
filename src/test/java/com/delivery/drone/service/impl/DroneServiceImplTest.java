package com.delivery.drone.service.impl;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ResponseDto;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.repository.DroneRepository;
import com.delivery.drone.repository.FleetRepository;
import com.delivery.drone.repository.MedicationRepository;
import com.delivery.drone.util.EnumUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * created by Diluni
 * on 8/26/2022
 */
@ExtendWith(SpringExtension.class)
class DroneServiceImplTest {
    @InjectMocks
    private DroneServiceImpl droneService;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private FleetRepository fleetRepository;

    @BeforeEach
    void setUp() {
        Optional<Fleet> fleet1 = Optional.of(new Fleet(1L, "fleet-1", 8));
        Optional<Fleet> fleet2 = Optional.of(new Fleet(2L, "fleet-2", 10));
        when(fleetRepository.findById(1L)).thenReturn(fleet1);
        when(fleetRepository.findById(2L)).thenReturn(fleet2);

        Optional<Drone> drone1 = Optional.of(new Drone("DC-13245", EnumUtil.Model.Cruiserweight, 300.0, 20.0, 46, fleet1.get()));
        Optional<Drone> drone2 = Optional.of(new Drone("DC-13246", EnumUtil.Model.Middleweight, 450.0, 450.0, 20, fleet1.get()));
        Optional<Drone> drone3 = Optional.of(new Drone("DC-13247", EnumUtil.Model.Heavyweight, 200.0, 0.0, 46, fleet1.get()));
        when(droneRepository.findBySerialNo("DC-13245")).thenReturn(drone1);
        when(droneRepository.findBySerialNo("DC-13246")).thenReturn(drone2);
        when(droneRepository.findBySerialNo("DC-13247")).thenReturn(drone3);

        when(medicationRepository.saveAllAndFlush(any())).thenReturn(new ArrayList<>());
    }

    /**
     * Success scenario of add drone
     */
    @Test
    void add() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.CREATED, "Drone has been registered successfully!");
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.Middleweight, 500.0, 70, 1L);
        ResponseDto actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Fleet details null scenario of add drone
     */
    @Test
    void addNullFleet() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.BAD_REQUEST, "Invalid Fleet Id or fleet is already filled with 10 drones! Drone registration is failed!");
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.Middleweight, 500.0, 70, 3L);
        ResponseDto actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Number of drones greater than 10 scenario of add drone
     */
    @Test
    void addNoOfDronesExceedFleet() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.BAD_REQUEST, "Invalid Fleet Id or fleet is already filled with 10 drones! Drone registration is failed!");
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.Middleweight, 500.0, 70, 2L);
        ResponseDto actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Exception scenario of add drone
     */
    @Test
    void addException() {
        when(droneRepository.save(any())).thenThrow(new RuntimeException());
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.Middleweight, 500.0, 70, 1L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneService.add(createDroneDto1);
        });
        assertEquals("Exception is occurred while registering the drone", exception.getMessage());
    }

    /**
     * Success scenario of load drone
     */
    @Test
    void load() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.CREATED, "Drone has been loaded successfully!");
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        ResponseDto actualResult = droneService.load("DC-13245", medicationDtos);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Drone details null scenario of load drone
     */
    @Test
    void loadNullDrone() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.BAD_REQUEST, "Drone loading is failed! Make sure whether Drone Serial Number is correct, Drone battery capacity is not less than 25% and Drone already is not fully loaded!");
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        ResponseDto actualResult = droneService.load("DC-13000", medicationDtos);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Drone battery capacity less than 25 scenario of load drone
     */
    @Test
    void loadLessBatteryCapacity() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.BAD_REQUEST, "Drone loading is failed! Make sure whether Drone Serial Number is correct, Drone battery capacity is not less than 25% and Drone already is not fully loaded!");
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        ResponseDto actualResult = droneService.load("DC-13246", medicationDtos);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Drone no available weight scenario of load drone
     */
    @Test
    void loadNoAvailableWeight() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.BAD_REQUEST, "Drone loading is failed! Make sure whether Drone Serial Number is correct, Drone battery capacity is not less than 25% and Drone already is not fully loaded!");
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        ResponseDto actualResult = droneService.load("DC-13247", medicationDtos);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Medication weights greater than drone's available weight scenario of load drone
     */
    @Test
    void loadMedWeightGreaterThan() {
        ResponseDto expectedResult = new ResponseDto(HttpStatus.NOT_ACCEPTABLE, "Drone loading is failed! Drone does not have enough space for entered medications weight!");
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 40.0, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        ResponseDto actualResult = droneService.load("DC-13245", medicationDtos);
        assertEquals(expectedResult.getStatus(), actualResult.getStatus());
    }

    /**
     * Exception scenario of load drone
     */
    @Test
    void loadException() {
        when(medicationRepository.saveAllAndFlush(any())).thenThrow(new RuntimeException());
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.0, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneService.load("DC-13245", medicationDtos);
        });
        assertEquals("Exception is occurred while loading the drone", exception.getMessage());
    }

    /**
     * Success scenario of get all available drones
     */
    @Test
    void getAll() {
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.Middleweight, 500.0, 70, 1L);
        List<CreateDroneDto> droneDtos = List.of(createDroneDto1);
        when(droneRepository.findAllAvailableDroneDtos()).thenReturn(droneDtos);

        List<CreateDroneDto> actualResult = droneService.getAll();
        assertEquals(1, actualResult.size());
    }

    /**
     * Exception scenario of get all available drones
     */
    @Test
    void getAllException() {
        when(droneRepository.findAllAvailableDroneDtos()).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneService.getAll();
        });
        assertEquals("Exception is occurred while getting available drones for loading", exception.getMessage());
    }

    /**
     * Success scenario of get battery level of drone
     */
    @Test
    void getBatteryLevel() {
        String actualResult = droneService.getBatteryLevel("DC-13245");
        assertEquals("46%", actualResult);

    }

    /**
     * Drone null scenario of get battery level of drone
     */
    @Test
    void getBatteryLevelDroneNull() {
        String actualResult = droneService.getBatteryLevel("DC-13000");
        assertEquals("Invalid drone serial number!", actualResult);

    }

}