package com.delivery.drone.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Entity
@Data
public class Medication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medId;

    private String name;

    private Double weight = 0.0;

    private String code;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "serial_no")
    private Drone drone;

    public Medication() {
    }

    public Medication(String name, Double weight, String code, String imagePath, Drone drone) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imagePath = imagePath;
        this.drone = drone;
    }

}