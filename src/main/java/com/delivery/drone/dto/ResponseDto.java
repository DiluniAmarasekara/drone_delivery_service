package com.delivery.drone.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * created by Diluni
 * on 8/26/2022
 */
@Data
public class ResponseDto {
    private HttpStatus status;
    private String message;

    public ResponseDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
