package com.delivery.drone.config;

import com.delivery.drone.entity.BatteryHistory;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.repository.BatteryHistoryRepository;
import com.delivery.drone.repository.DroneRepository;
import com.delivery.drone.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * created by Diluni
 * on 8/25/2022
 */
@Configuration
@EnableScheduling
public class DroneConfig {
    Logger logger = LoggerFactory.getLogger(DroneConfig.class);

    final Map<EnumUtil.State, EnumUtil.State> stateContinuation = new HashMap<>() {{
        put(EnumUtil.State.LOADED, EnumUtil.State.DELIVERING);
        put(EnumUtil.State.DELIVERING, EnumUtil.State.DELIVERED);
        put(EnumUtil.State.DELIVERED, EnumUtil.State.RETURNING);
        put(EnumUtil.State.RETURNING, EnumUtil.State.IDLE);
    }};

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private BatteryHistoryRepository batteryHistoryRepository;

    /**
     * Periodic method to check drones battery levels and create history/audit event log in each 10 minutes
     */
    @Scheduled(fixedRate = 10, initialDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void batteryAudit() {
        logger.info("Battery Audit scheduler is running at " + new Date());
        List<Drone> drones = droneRepository.findAll();
        if (drones != null) {
            try {
                List<BatteryHistory> batteryHistories = drones.stream()
                        .map(drone -> new BatteryHistory(drone.getBatteryCapacity(), drone)).collect(Collectors.toList());
                batteryHistoryRepository.saveAll(batteryHistories);
            } catch (Exception e) {
                logger.info("Encountered with an exception at " + new Date() + " " + e.getMessage());
                throw new RuntimeException("Exception is occurred while running the Battery Audit scheduler");
            }
            logger.info("Battery Audit scheduler is finished at " + new Date());
        } else {
            logger.info("No drones available to log the history!");
        }
        logger.info("Battery Audit scheduler exit at " + new Date());
    }

    /**
     * Periodic method to update drones battery levels in each 5 minutes
     */
    @Scheduled(fixedRate = 5, initialDelay = 3, timeUnit = TimeUnit.MINUTES)
    public void batteryPercentageUpdate() {
        logger.info("Battery Percentage Update scheduler is running at " + new Date());
        List<Drone> drones = droneRepository.findAll();
        if (drones != null) {
            try {
                drones.forEach(drone -> drone.setBatteryCapacity(drone.getBatteryCapacity() - 1));
                droneRepository.saveAll(drones);
            } catch (Exception e) {
                logger.info("Encountered with an exception at " + new Date() + " " + e.getMessage());
                throw new RuntimeException("Exception is occurred while running the Battery Percentage Update scheduler");
            }
            logger.info("Battery Percentage Update scheduler is finished at " + new Date());
        } else {
            logger.info("No drones available to update battery percentage!");
        }
        logger.info("Battery Percentage Update scheduler exit at " + new Date());
    }

    /**
     * Periodic method to continue update state of not available drones in each 15 minutes
     * LOADED->DELIVERING->DELIVERED->RETURNING->IDLE
     */
    @Scheduled(fixedRate = 15, initialDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void notAvailableDroneStateUpdate() {
        logger.info("Not Available Drone State Update scheduler is running at " + new Date());
        List<Drone> drones = droneRepository.findByStateNotIn(Arrays.asList(EnumUtil.State.IDLE, EnumUtil.State.LOADING));
        if (drones != null) {
            try {
                drones.forEach(drone -> drone.setState(stateContinuation.get(drone.getState())));
                droneRepository.saveAll(drones);
            } catch (Exception e) {
                logger.info("Encountered with an exception at " + new Date() + " " + e.getMessage());
                throw new RuntimeException("Exception is occurred while running the Not Available Drones State Update scheduler");
            }
            logger.info("Not Available Drone State Update scheduler is finished at " + new Date());
        } else {
            logger.info("No non-available drones to update the state!");
        }
        logger.info("Not Available Drone State Update scheduler exit at " + new Date());
    }
}