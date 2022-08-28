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

    //    Use to track the order of drone's state cycle
    final Map<EnumUtil.State, EnumUtil.State> stateCycle = new HashMap<>() {{
        put(EnumUtil.State.LOADING, EnumUtil.State.LOADED);
        put(EnumUtil.State.LOADED, EnumUtil.State.DELIVERING);
        put(EnumUtil.State.DELIVERING, EnumUtil.State.DELIVERED);
        put(EnumUtil.State.DELIVERED, EnumUtil.State.RETURNING);
        put(EnumUtil.State.RETURNING, EnumUtil.State.IDLE);
    }};


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
//                    Initially state sets as IDLE and full weight is available to load
                    Drone drone = new Drone(droneDto.getSerialNo(), EnumUtil.Model.valueOf(droneDto.getModel().toUpperCase()), Double.parseDouble(String.valueOf(droneDto.getAvailableWeight())),
                            Double.parseDouble(String.valueOf(droneDto.getAvailableWeight())), droneDto.getBatteryCapacity(), EnumUtil.State.IDLE, fleet.get());
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
        if (drone.isPresent() && drone.get().getBatteryCapacity() >= 25 &&
                (drone.get().getState().equals(EnumUtil.State.IDLE) || drone.get().getState().equals(EnumUtil.State.LOADING))) {
            Double medsWeight = medicationDtos.stream().mapToDouble(MedicationDto::getWeight).sum();
            if (medsWeight <= drone.get().getAvailableWeight()) {
                try {
                    List<Medication> medicationList = medicationDtos.stream()
                            .map(medicationDto -> new Medication(medicationDto.getName(), medicationDto.getWeight(), medicationDto.getCode(),
                                    medicationDto.getImagePath(), drone.get())).collect(Collectors.toList());
                    medicationRepository.saveAll(medicationList);
                    drone.get().setAvailableWeight(drone.get().getAvailableWeight() - medsWeight);
//                    State sets as LOADED if no weight available to load else LOADING can continue loading again
                    drone.get().setState(drone.get().getAvailableWeight() == 0 ? EnumUtil.State.LOADED : EnumUtil.State.LOADING);
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

    /**
     * Implementation of update drone status LOADED->DELIVERING->DELIVERED->RETURNING->IDLE
     *
     * @param serialNo
     * @return Map
     */
    @Transactional
    @Override
    public Map<Boolean, String> updateState(String serialNo) {
        logger.info("Enter the update state service implementation");
        Optional<Drone> drone = droneRepository.findBySerialNo(serialNo);
        if (drone.isPresent() && !drone.get().getState().equals(EnumUtil.State.IDLE)) {
            try {
                if (stateCycle.get(drone.get().getState()).equals(EnumUtil.State.IDLE)) {
//                If RETURNING state updates into IDLE state then drone available weight should be reset
//                and all the not delivered med items status should be changed into isDelivered true
                    List<Medication> medications = medicationRepository.findAllByDrone_SerialNoAndIsDelivered(serialNo, false);
                    medications.forEach(med -> med.setIsDelivered(Boolean.TRUE));
                    medicationRepository.saveAll(medications);

                    drone.get().setAvailableWeight(drone.get().getWeightLimit());
                    logger.info("Drone returns and now 0 loaded items and full weight is available to load new med items");
                }
                drone.get().setState(stateCycle.get(drone.get().getState()));
                droneRepository.save(drone.get());
                logger.info("Exit the update state service implementation");
                return new HashMap<>() {{
                    put(true, "Drone state is successfully updated into " + drone.get().getState().name());
                }};
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Exception is occurred while updating the state");
            }
        } else {
            logger.info("Exit the update state service implementation");
            return new HashMap<>() {{
                put(false, "Invalid drone serial number or drone state is IDLE! If IDLE please start the loading process and try!");
            }};
        }
    }

}