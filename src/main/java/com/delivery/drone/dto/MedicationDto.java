package com.delivery.drone.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
@NonNull
public class MedicationDto {
    @Pattern(regexp = "[A-Za-z0-9--_]", message = "Invalid medication name format!")
    private String name;

    @Min(value = 0, message = "Invalid Weight!")
    private Double weight;

    //    need regex
    private String code;

    private String imagePath;

    public MedicationDto(String name, Double weight, String code, String imagePath) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imagePath = imagePath;
    }

}
