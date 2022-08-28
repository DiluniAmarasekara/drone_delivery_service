package com.delivery.drone.util;

/**
 * created by Diluni
 * on 8/26/2022
 */
public class EnumUtil {
    public enum Model {
        LW("Lightweight"),
        MW("Middleweight"),
        CW("Cruiserweight"),
        HW("Heavyweight");

        String value;

        Model(String value) {
            this.value = value;
        }
    }

    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

}