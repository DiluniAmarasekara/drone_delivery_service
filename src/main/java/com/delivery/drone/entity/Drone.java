package com.delivery.drone.entity;

import com.delivery.drone.util.EnumUtil;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Entity
@Data
public class Drone extends BaseEntity {
    @Id
    @Size(max = 100, message = "Serial Number should be max 100 characters!")
    private String serialNo;

    @Enumerated(EnumType.ORDINAL)
    private EnumUtil.Model model;

    @Max(value = 500, message = "Invalid data! Weight should be no more than 500g!")
    @Min(value = 0, message = "Invalid data! Weight should be a positive value!")
    private Double weightLimit;

    private Double availableWeight;

    @Max(value = 100, message = "Invalid Battery Capacity!")
    @Min(value = 0, message = "Invalid Battery Capacity!")
    private Integer batteryCapacity;

    @Enumerated(EnumType.ORDINAL)
    private EnumUtil.State state;

    @ManyToOne
    @JoinColumn(name = "fleet_id", nullable = false)
    private Fleet fleet;

    public Drone() {
    }

    public Drone(String serialNo, EnumUtil.Model model, Double weightLimit, Double availableWeight, Integer batteryCapacity, Fleet fleet) {
        this.serialNo = serialNo;
        this.model = model;
        this.weightLimit = weightLimit;
        this.availableWeight = availableWeight;
        this.batteryCapacity = batteryCapacity;
        this.fleet = fleet;
    }

    /**
     * Set status of the drone just before inserting and updating
     */
    @PrePersist
    @PreUpdate
    private void beforeCreateOrUpdate() {
        if (this.getAvailableWeight() == this.weightLimit) {
            this.state = EnumUtil.State.IDLE;
        } else if (this.getAvailableWeight() > 0) {
            this.state = EnumUtil.State.LOADING;
        } else if (this.getAvailableWeight() == 0) {
            this.state = EnumUtil.State.LOADED;
        }
    }

}
