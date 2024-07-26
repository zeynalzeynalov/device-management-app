package org.abc.app.api;

import org.abc.app.device.Device;
import org.abc.app.device.DeviceCreateUpdateRequest;
import org.abc.app.device.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    public void create(DeviceCreateUpdateRequest request) {
        Device device = Device.builder().name(request.getName()).brand(request.getBrand()).build();
        deviceRepository.save(device);
    }
}
