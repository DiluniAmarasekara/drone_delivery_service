package com.delivery.drone.dto;

import com.delivery.drone.util.EnumUtil;
import lombok.Data;

import java.util.List;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
public class DroneDto {
    private String serialNo;

    private EnumUtil.Model model;

    private Double weight;

    private Integer batteryCapacity;

    private EnumUtil.State state;

    private Long fleetId;

    public DroneDto() {
    }

    public DroneDto(String serialNo, EnumUtil.Model model, Double weight, Integer batteryCapacity, EnumUtil.State state, Long fleetId) {
        this.serialNo = serialNo;
        this.model = model;
        this.weight = weight;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
        this.fleetId = fleetId;
    }
}