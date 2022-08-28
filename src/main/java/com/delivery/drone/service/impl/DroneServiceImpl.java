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
     * @return Boolean
     */
    @Transactional
    @Override
    public Boolean add(CreateDroneDto droneDto) {
        Optional<Fleet> fleet = fleetRepository.findById(droneDto.getFleetId());
        if (fleet.isPresent() && fleet.get().getNoOfDrones() < 10) {
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
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
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
    public Boolean load(String serialNo, List<MedicationDto> medicationDtos) {
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
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    /**
     * Implementation of checking available drones for loading
     *
     * @return List<CreateDroneDto>
     */
    @Override
    public List<DroneDto> getAll() {
        List<DroneDto> droneDtos;
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
