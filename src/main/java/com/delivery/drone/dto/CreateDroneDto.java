package com.delivery.drone.dto;

import com.delivery.drone.util.EnumUtil;
import com.delivery.drone.validator.custom.ValidateEnum;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
public class CreateDroneDto {
    @Size(max = 100, message = "Serial Number should be max 100 characters!")
    private String serialNo;

    @ValidateEnum(enumClazz = EnumUtil.Model.class, message = "Model should be one from LW, MW, CW, HW!")
    private String model;

    @DecimalMax(value = "500.0", message = "Weight should be no more than 500g!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight should be greater than 0g!")
//    @Digits(integer=3, fraction=2)
    private Double availableWeight;

    @Max(value = 100, message = "Invalid Battery Capacity!")
    @Min(value = 0, message = "Invalid Battery Capacity!")
    private Integer batteryCapacity;

    @DecimalMin(value = "0", inclusive = false, message = "Fleet Id is invalid!")
    private Long fleetId;

    public CreateDroneDto() {
    }

    public CreateDroneDto(String serialNo, String model, Double availableWeight, Integer batteryCapacity, Long fleetId) {
        this.serialNo = serialNo;
        this.model = model;
        this.availableWeight = availableWeight;
        this.batteryCapacity = batteryCapacity;
        this.fleetId = fleetId;
    }
}