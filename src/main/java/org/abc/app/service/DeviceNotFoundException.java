package org.abc.app.service;

public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(long deviceId) {
        super(String.format("Device with id=%d not found", deviceId));
    }
}
