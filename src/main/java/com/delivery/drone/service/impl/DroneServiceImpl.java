package com.delivery.drone.service.impl;

import com.delivery.drone.dto.CreateDroneDto;
import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.dto.ResponseDto;
import com.delivery.drone.entity.Drone;
import com.delivery.drone.entity.Fleet;
import com.delivery.drone.entity.Medication;
import com.delivery.drone.repository.DroneRepository;
import com.delivery.drone.repository.FleetRepository;
import com.delivery.drone.repository.MedicationRepository;
import com.delivery.drone.service.DroneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * @return ResponseDto
     */
    @Transactional
    @Override
    public ResponseDto add(CreateDroneDto droneDto) {
        Optional<Fleet> fleet = fleetRepository.findById(droneDto.getFleetId());
        if (fleet.isPresent() && fleet.get().getNoOfDrones() < 10) {
            try {
                Drone drone = new Drone(droneDto.getSerialNo(), droneDto.getModel(), droneDto.getAvailableWeight(),
                        droneDto.getAvailableWeight(), droneDto.getBatteryCapacity(), fleet.get());
                droneRepository.save(drone);
                fleet.get().setNoOfDrones(fleet.get().getNoOfDrones() + 1);
                fleetRepository.saveAndFlush(fleet.get());
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException("Exception is occurred while registering the drone");
            }
            return new ResponseDto(HttpStatus.CREATED, "Drone has been registered successfully!");
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST, "Invalid Fleet Id or fleet is already filled with 10 drones! Drone registration is failed!");
    }

    /**
     * Implementation of loading a drone with medication items
     *
     * @param serialNo
     * @param medicationDtos
     * @return ResponseDto
     */
    @Transactional
    @Override
    public ResponseDto load(String serialNo, List<MedicationDto> medicationDtos) {
        Optional<Drone> drone = droneRepository.findBySerialNo(serialNo);
        if (drone.isPresent() && drone.get().getBatteryCapacity() >= 25 && drone.get().getAvailableWeight() > 0) {
            Double medsWeight = medicationDtos.stream().mapToDouble(MedicationDto::getWeight).sum();
            if (medsWeight <= drone.get().getAvailableWeight()) {
                try {
                    List<Medication> medicationList = medicationDtos.stream()
                            .map(medicationDto -> new Medication(medicationDto.getName(), medicationDto.getWeight(), medicationDto.getCode(),
                                    medicationDto.getImagePath(), drone.get())).collect(Collectors.toList());
                    medicationRepository.saveAllAndFlush(medicationList);
                    drone.get().setAvailableWeight(drone.get().getAvailableWeight() - medsWeight);
                    droneRepository.saveAndFlush(drone.get());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException("Exception is occurred while loading the drone");
                }
                return new ResponseDto(HttpStatus.CREATED, "Drone has been loaded successfully!");
            }
            return new ResponseDto(HttpStatus.NOT_ACCEPTABLE, "Drone loading is failed! Drone does not have enough space for entered medications weight!");
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST, "Drone loading is failed! Make sure whether Drone Serial Number is correct, Drone battery capacity is not less than 25% and Drone already is not fully loaded!");
    }

    /**
     * Implementation of checking available drones for loading
     *
     * @return List<CreateDroneDto>
     */
    @Override
    public List<CreateDroneDto> getAll() {
        List<CreateDroneDto> droneDtos;
        try {
            droneDtos = droneRepository.findAllAvailableDroneDtos();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Exception is occurred while getting available drones for loading");
        }
        return droneDtos;
    }

    /**
     * Implementation of check drone battery level for a given drone
     *
     * @param serialNo
     * @return String
     */
    @Override
    public String getBatteryLevel(String serialNo) {
        Optional<Drone> drone = droneRepository.findBySerialNo(serialNo);
        if (drone.isPresent()) {
            return drone.get().getBatteryCapacity() + "%";
        }
        return "Invalid drone serial number!";
    }

}
