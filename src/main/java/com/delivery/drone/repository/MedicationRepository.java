package com.delivery.drone.repository;

import com.delivery.drone.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * created by Diluni
 * on 8/27/2022
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
