package com.delivery.drone.util;

/**
 * created by Diluni
 * on 8/26/2022
 */
public class EnumUtil {
    public enum Model {
        Lightweight, Middleweight, Cruiserweight, Heavyweight
    }

    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

}
