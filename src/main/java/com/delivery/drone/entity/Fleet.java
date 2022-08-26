package com.delivery.drone.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Entity
@Data
public class Fleet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fleetId;

    private String fleetName;

    private Integer noOfDrones;

    public Fleet() {
    }

    public Fleet(String fleetName, Integer noOfDrones) {
        this.fleetName = fleetName;
        this.noOfDrones = noOfDrones;
    }

    public Fleet(Long fleetId, String fleetName, Integer noOfDrones) {
        this.fleetId = fleetId;
        this.fleetName = fleetName;
        this.noOfDrones = noOfDrones;
    }

    //    other relevant attributes
}
