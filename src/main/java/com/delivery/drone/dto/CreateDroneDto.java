package com.delivery.drone.dto;

import com.delivery.drone.util.EnumUtil;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
@NonNull
public class CreateDroneDto {
    @Size(max = 100, message = "Serial Number should be max 100 characters!")
    private String serialNo;

    private EnumUtil.Model model;

    private Double availableWeight;

    private Integer batteryCapacity;

    private Long fleetId;

    public CreateDroneDto() {
    }

    public CreateDroneDto(String serialNo, EnumUtil.Model model, Double availableWeight, Integer batteryCapacity, Long fleetId) {
        this.serialNo = serialNo;
        this.model = model;
        this.availableWeight = availableWeight;
        this.batteryCapacity = batteryCapacity;
        this.fleetId = fleetId;
    }

}
