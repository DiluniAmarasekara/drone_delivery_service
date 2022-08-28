package com.delivery.drone.service.impl;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.DroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.entity.Medication;
import com.delivery.drone.repository.DroneRepository;
import com.delivery.drone.repository.FleetRepository;
import com.delivery.drone.repository.MedicationRepository;
import com.delivery.drone.util.EnumUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * created by Diluni
 * on 8/26/2022
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(OutputCaptureExtension.class)
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
        Optional<Fleet> fleet1 = Optional.of(new Fleet(1L, "fleet-1", 6));
        Optional<Fleet> fleet2 = Optional.of(new Fleet(2L, "fleet-2", 10));
        when(fleetRepository.findById(1L)).thenReturn(fleet1);
        when(fleetRepository.findById(2L)).thenReturn(fleet2);

        Optional<Drone> drone1 = Optional.of(new Drone("DC-13245", EnumUtil.Model.CW, 300.0, 20.0, 46, EnumUtil.State.IDLE, fleet1.get()));
        Optional<Drone> drone2 = Optional.of(new Drone("DC-13246", EnumUtil.Model.MW, 450.0, 450.0, 20, EnumUtil.State.LOADING, fleet1.get()));
        Optional<Drone> drone3 = Optional.of(new Drone("DC-13247", EnumUtil.Model.HW, 200.0, 0.0, 60, EnumUtil.State.DELIVERING, fleet1.get()));
        Optional<Drone> drone4 = Optional.of(new Drone("DC-13248", EnumUtil.Model.LW, 100.0, 2.0, 30, EnumUtil.State.RETURNING, fleet1.get()));
        when(droneRepository.findBySerialNo("DC-13245")).thenReturn(drone1);
        when(droneRepository.findBySerialNo("DC-13246")).thenReturn(drone2);
        when(droneRepository.findBySerialNo("DC-13247")).thenReturn(drone3);
        when(droneRepository.findBySerialNo("DC-13248")).thenReturn(drone4);
    }

    /**
     * Success scenario of add drone
     */
    @Test
    void add() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(true, "Drone has been registered successfully!");
        }};
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13249", EnumUtil.Model.MW.toString(), 500.0, 70, 1L);
        Map<Boolean, String> actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(true), actualResult.get(true));
    }

    /**
     * Fleet details null scenario of add drone
     */
    @Test
    void addNullFleet() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Fleet is not exist or fleet already filled with 10 drones!");
        }};
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.MW.toString(), 500.0, 70, 3L);
        Map<Boolean, String> actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Number of drones greater than 10 scenario of add drone
     */
    @Test
    void addNoOfDronesExceedFleet() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Fleet is not exist or fleet already filled with 10 drones!");
        }};
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.MW.toString(), 500.0, 70, 2L);
        Map<Boolean, String> actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Serial number is already exist scenario of add drone
     */
    @Test
    void addAlreadyExist() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Drone serial number is already exist!");
        }};
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13245", EnumUtil.Model.MW.toString(), 500.0, 70, 1L);
        Map<Boolean, String> actualResult = droneService.add(createDroneDto1);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Exception scenario of add drone
     */
    @Test
    void addException() {
        when(droneRepository.save(any())).thenThrow(new RuntimeException());
        CreateDroneDto createDroneDto1 = new CreateDroneDto("DC-13249", EnumUtil.Model.MW.toString(), 500.0, 70, 1L);
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
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(true, "Drone loading is successful!");
        }};
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        Map<Boolean, String> actualResult = droneService.load("DC-13245", medicationDtos);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(true), actualResult.get(true));
    }

    /**
     * Drone details null scenario of load drone
     */
    @Test
    void loadNullDrone() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Drone is not exist or drone battery capacity less than 25% or drone is already loaded!");
        }};
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        Map<Boolean, String> actualResult = droneService.load("DC-13000", medicationDtos);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Drone battery capacity less than 25 scenario of load drone
     */
    @Test
    void loadLessBatteryCapacity() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Drone is not exist or drone battery capacity less than 25% or drone is already loaded!");
        }};
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        Map<Boolean, String> actualResult = droneService.load("DC-13246", medicationDtos);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Drone no available weight scenario of load drone
     */
    @Test
    void loadNotAvailableDrone() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Drone is not exist or drone battery capacity less than 25% or drone is already loaded!");
        }};
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 10.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        Map<Boolean, String> actualResult = droneService.load("DC-13247", medicationDtos);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Medication weights greater than drone's available weight scenario of load drone
     */
    @Test
    void loadMedWeightGreaterThan() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Medications weight is greater than Drone's available weight!");
        }};
        MedicationDto medicationDto1 = new MedicationDto("MED-1", 40.5, "HH_GR6", "/this/folder/image.png");
        List<MedicationDto> medicationDtos = List.of(medicationDto1);
        Map<Boolean, String> actualResult = droneService.load("DC-13245", medicationDtos);
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Exception scenario of load drone
     */
    @Test
    void loadException() {
        when(medicationRepository.saveAll(any())).thenThrow(new RuntimeException());
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
        DroneDto DroneDto1 = new DroneDto("DC-13245", EnumUtil.Model.MW, 500.0, 70, EnumUtil.State.IDLE, 1L);
        DroneDto DroneDto2 = new DroneDto("DC-13246", EnumUtil.Model.CW, 360.0, 50, EnumUtil.State.LOADING, 1L);
        List<DroneDto> droneDtos = List.of(DroneDto1, DroneDto2);
        when(droneRepository.findAllAvailableDroneDtos()).thenReturn(droneDtos);
        List<DroneDto> actualResult = droneService.getAll();
        assertEquals(2, actualResult.size());
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
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(true, "46%");
        }};
        Map<Boolean, String> actualResult = droneService.getBatteryLevel("DC-13245");
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(true), actualResult.get(true));
    }

    /**
     * Drone null scenario of get battery level of drone
     */
    @Test
    void getBatteryLevelDroneNull() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Invalid drone serial number!");
        }};
        Map<Boolean, String> actualResult = droneService.getBatteryLevel("DC-13000");
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Success scenario of update state
     */
    @Test
    void updateState() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(true, "Drone state is successfully updated into DELIVERED");
        }};
        Map<Boolean, String> actualResult = droneService.updateState("DC-13247");
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(true), actualResult.get(true));
    }

    /**
     * Drone null scenario of update state
     */
    @Test
    void updateStateDroneNull() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Invalid drone serial number or drone state is IDLE! If IDLE please start the loading process and try!");
        }};
        Map<Boolean, String> actualResult = droneService.updateState("DC-13249");
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Before update, drone state IDLE scenario of update state
     */
    @Test
    void updateStateExistIdleState() {
        Map<Boolean, String> expectedResult = new HashMap<>() {{
            put(false, "Invalid drone serial number or drone state is IDLE! If IDLE please start the loading process and try!");
        }};
        Map<Boolean, String> actualResult = droneService.updateState("DC-13245");
        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(false), actualResult.get(false));
    }

    /**
     * Before update, drone state RETURNING scenario of update state
     */
    @Test
    void updateStateIntoIdle(CapturedOutput output) {
        Medication medication1 = new Medication(1L, "MED-1", 40.0, "HH_GR6", "/this/folder/image.png", false, new Drone());
        List<Medication> medications = List.of(medication1);
        when(medicationRepository.findAllByDrone_SerialNoAndIsDelivered("DC-13248", false)).thenReturn(medications);
        droneService.updateState("DC-13248");
        verify(medicationRepository).saveAll(medications);
        Assertions.assertTrue(output.getOut().contains("Drone returns and now 0 loaded items and full weight is available to load new med items"));
    }

    /**
     * Exception scenario of load drone
     */
    @Test
    void updateStateException() {
        when(droneRepository.save(any())).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneService.updateState("DC-13247");
        });
        assertEquals("Exception is occurred while updating the state", exception.getMessage());
    }

}