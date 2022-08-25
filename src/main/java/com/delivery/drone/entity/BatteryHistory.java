package com.delivery.drone.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Entity
@Data
public class BatteryHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer batteryCapacity;

    @ManyToOne
    @JoinColumn(name = "serial_no")
    private Drone drone;

    public BatteryHistory() {
    }

    public BatteryHistory(Integer batteryCapacity, Drone drone) {
        this.batteryCapacity = batteryCapacity;
        this.drone = drone;
    }

}
