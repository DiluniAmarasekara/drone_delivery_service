package com.delivery.drone.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
public class MedicationDto {
    @Pattern(regexp = "^[\\w\\-]*$", message = "Invalid medication name! This allows only letters, numbers, ‘-‘, ‘_’")
    private String name;

    @DecimalMax(value = "500.0", message = "Weight should be no more than 500g!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight should be greater than 0g!")
    private Double weight;

    @Pattern(regexp = "^[A-Z_0-9]*$", message = "Invalid medication code! This allows only upper case letters, underscore and numbers")
    private String code;

    private String imagePath;

    public MedicationDto() {
    }

    public MedicationDto(String name, Double weight, String code, String imagePath) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imagePath = imagePath;
    }

}