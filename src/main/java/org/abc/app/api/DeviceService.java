package org.abc.app.api;

import org.abc.app.device.Device;
import org.abc.app.device.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAll() {

        //TODO: implement method body

        return new ArrayList<>();
    }
}
