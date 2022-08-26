package com.delivery.drone.repository;

import com.delivery.drone.dto.MedicationDto;
import com.delivery.drone.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by Diluni
 * on 8/27/2022
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    @Query("SELECT DISTINCT new com.delivery.drone.dto.MedicationDto(b.name, b.weight, b.code, b.imagePath) FROM Drone a LEFT JOIN Medication b ON a.serialNo=b.drone.serialNo WHERE b.drone.serialNo=:serialNo")
    List<MedicationDto> findAllMedicationDtos(String serialNo);

}
