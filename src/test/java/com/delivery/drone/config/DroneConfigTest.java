package com.delivery.drone.config;

import com.delivery.drone.entity.BatteryHistory;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.repository.BatteryHistoryRepository;
import com.delivery.drone.repository.DroneRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * created by Diluni
 * on 8/26/2022
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class DroneConfigTest {
    @InjectMocks
    private DroneConfig droneConfig;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private BatteryHistoryRepository batteryHistoryRepository;

    List<Drone> drones = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Optional<Fleet> fleet1 = Optional.of(new Fleet(1L, "fleet-1", 8));

        Optional<Drone> drone1 = Optional.of(new Drone("DC-13245", EnumUtil.Model.CW, 300.0, 20.0, 46, fleet1.get()));
        Optional<Drone> drone2 = Optional.of(new Drone("DC-13246", EnumUtil.Model.MW, 450.0, 450.0, 20, fleet1.get()));
        Optional<Drone> drone3 = Optional.of(new Drone("DC-13247", EnumUtil.Model.HW, 200.0, 0.0, 46, fleet1.get()));

        drones = List.of(drone1.get(), drone2.get(), drone3.get());
        when(droneRepository.findAll()).thenReturn(drones);
    }

    /**
     * Success scenario of battery audit
     */
    @Test
    void batteryAudit(CapturedOutput output) {
        List<BatteryHistory> batteryHistories = drones.stream()
                .map(drone -> new BatteryHistory(drone.getBatteryCapacity(), drone)).collect(Collectors.toList());
        droneConfig.batteryAudit();
        verify(batteryHistoryRepository).saveAll(batteryHistories);
        Assertions.assertTrue(output.getOut().contains("Battery Audit scheduler is finished at"));
    }

    /**
     * Drones null scenario of battery audit
     */
    @Test
    void batteryAuditDronesNullDrones(CapturedOutput output) {
        when(droneRepository.findAll()).thenReturn(null);
        droneConfig.batteryAudit();
        Assertions.assertTrue(output.getOut().contains("No drones available to log the history!"));
    }

    /**
     * Exception scenario of battery audit
     */
    @Test
    void batteryAuditException() {
        when(batteryHistoryRepository.saveAll(any())).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneConfig.batteryAudit();
        });
        assertEquals("Exception is occurred while running the Battery Audit scheduler", exception.getMessage());
    }

    /**
     * Success scenario of battery percentage update
     */
    @Test
    void batteryPercentageUpdate(CapturedOutput output) {
        droneConfig.batteryPercentageUpdate();
        verify(droneRepository).saveAll(drones);
        Assertions.assertTrue(output.getOut().contains("Battery Percentage Update scheduler is finished at"));
    }

    /**
     * Drones null scenario of battery percentage update
     */
    @Test
    void batteryPercentageUpdateNullDrones(CapturedOutput output) {
        when(droneRepository.findAll()).thenReturn(null);
        droneConfig.batteryPercentageUpdate();
        Assertions.assertTrue(output.getOut().contains("No drones available to update battery percentage!"));
    }

    /**
     * Exception scenario of battery percentage update
     */
    @Test
    void batteryPercentageUpdateException() {
        when(droneRepository.saveAll(any())).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneConfig.batteryPercentageUpdate();
        });
        assertEquals("Exception is occurred while running the Battery Percentage Update scheduler", exception.getMessage());
    }

    /**
     * Success scenario of not available drone's state update
     */
    @Test
    void notAvailableDroneStateUpdate(CapturedOutput output) {
        droneConfig.notAvailableDroneStateUpdate();
        Assertions.assertTrue(output.getOut().contains("Not Available Drone State Update scheduler is finished at"));
    }

    /**
     * Drones null scenario of not available drone's state update
     */
    @Test
    void notAvailableDroneStateUpdateNullDrones(CapturedOutput output) {
        when(droneRepository.findByStateNotIn(any())).thenReturn(null);
        droneConfig.notAvailableDroneStateUpdate();
        Assertions.assertTrue(output.getOut().contains("No non-available drones to update the state!"));
    }

    /**
     * Exception scenario of not available drone's state update
     */
    @Test
    void notAvailableDroneStateUpdateException() {
        when(droneRepository.saveAll(any())).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneConfig.notAvailableDroneStateUpdate();
        });
        assertEquals("Exception is occurred while running the Not Available Drones State Update scheduler", exception.getMessage());
    }
}