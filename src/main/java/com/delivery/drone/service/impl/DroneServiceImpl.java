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
import com.delivery.drone.service.DroneService;
import com.delivery.drone.util.EnumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Service
public class DroneServiceImpl implements DroneService {
    Logger logger = LoggerFactory.getLogger(DroneServiceImpl.class);

    @Autowired
    private FleetRepository fleetRepository;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    /**
     * Implementation of registering a drone
     *
     * @param droneDto
     * @return Map
     */
    @Transactional
    @Override
    public Map<Boolean, String> add(CreateDroneDto droneDto) {
        logger.info("Enter the add drone service implementation");
        Optional<Fleet> fleet = fleetRepository.findById(droneDto.getFleetId());
        if (fleet.isPresent() && fleet.get().getNoOfDrones() < 10) {
            Optional<Drone> droneExist = droneRepository.findBySerialNo(droneDto.getSerialNo());
            if (!droneExist.isPresent()) {
                try {
                    Drone drone = new Drone(droneDto.getSerialNo(), EnumUtil.Model.valueOf(droneDto.getModel().toUpperCase()), Double.parseDouble(String.valueOf(droneDto.getAvailableWeight())),
                            Double.parseDouble(String.valueOf(droneDto.getAvailableWeight())), droneDto.getBatteryCapacity(), fleet.get());
                    droneRepository.save(drone);
                    fleet.get().setNoOfDrones(fleet.get().getNoOfDrones() + 1);
                    fleetRepository.save(fleet.get());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException("Exception is occurred while registering the drone");
                }
                logger.info("Exit the add drone service implementation");
                return new HashMap<>() {{
                    put(true, "Drone has been registered successfully!");
                }};
            } else {
                logger.info("Exit the add drone service implementation");
                return new HashMap<>() {{
                    put(false, "Drone serial number is already exist!");
                }};
            }
        }
        logger.info("Exit the add drone service implementation");
        return new HashMap<>() {{
            put(false, "Fleet is not exist or fleet already filled with 10 drones!");
        }};
    }

    /**
     * Implementation of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return Map
     */
    @Transactional
    @Override
    public Map<Boolean, String> load(String serialNo, List<MedicationDto> medicationDtos) {
        logger.info("Enter the load drone service implementation");
        Optional<Drone> drone = droneRepository.findBySerialNo(serialNo);
        if (drone.isPresent() && drone.get().getBatteryCapacity() >= 25 && drone.get().getAvailableWeight() > 0) {
            Double medsWeight = medicationDtos.stream().mapToDouble(MedicationDto::getWeight).sum();
            if (medsWeight <= drone.get().getAvailableWeight()) {
                try {
                    List<Medication> medicationList = medicationDtos.stream()
                            .map(medicationDto -> new Medication(medicationDto.getName(), medicationDto.getWeight(), medicationDto.getCode(),
                                    medicationDto.getImagePath(), drone.get())).collect(Collectors.toList());
                    medicationRepository.saveAll(medicationList);
                    drone.get().setAvailableWeight(drone.get().getAvailableWeight() - medsWeight);
                    droneRepository.save(drone.get());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException("Exception is occurred while loading the drone");
                }
                logger.info("Exit the load drone service implementation");
                return new HashMap<>() {{
                    put(true, "Drone loading is successful!");
                }};
            } else {
                logger.info("Exit the load drone service implementation");
                return new HashMap<>() {{
                    put(false, "Medications weight is greater than Drone's available weight!");
                }};
            }
        }
        logger.info("Exit the load drone service implementation");
        return new HashMap<>() {{
            put(false, "Drone is not exist or drone battery capacity less than 25% or drone is already loaded!");
        }};
    }

    /**
     * Implementation of checking available drones for loading
     *
     * @return List
     */
    @Override
    public List<DroneDto> getAll() {
        logger.info("Enter the get all available drones service implementation");
        List<DroneDto> droneDtos;
        try {
            droneDtos = droneRepository.findAllAvailableDroneDtos();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while getting available drones for loading");
        }
        logger.info("Exit the get all available drones service implementation");
        return droneDtos;
    }

    /**
     * Implementation of check drone battery level for a given drone
     *
     * @param serialNo
     * @return Map
     */
    @Override
    public Map<Boolean, String> getBatteryLevel(String serialNo) {
        logger.info("Enter the get battery level of drone service implementation");
        Optional<Drone> drone = droneRepository.findBySerialNo(serialNo);
        if (drone.isPresent()) {
            logger.info("Exit the get battery level of drone service implementation");
            return new HashMap<>() {{
                put(true, drone.get().getBatteryCapacity() + "%");
            }};
        }
        logger.info("Exit the get battery level of drone service implementation");
        return new HashMap<>() {{
            put(false, "Invalid drone serial number!");
        }};
    }

}