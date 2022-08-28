package com.delivery.drone.dto;

import lombok.Data;

/**
 * created by Diluni
 * on 8/28/2022
 */
@Data
public class FleetDto {
    private Long fleetId;

    private String fleetName;

    private Integer noOfDrones;

    public FleetDto() {
    }

    public FleetDto(Long fleetId, String fleetName, Integer noOfDrones) {
        this.fleetId = fleetId;
        this.fleetName = fleetName;
        this.noOfDrones = noOfDrones;
    }

}