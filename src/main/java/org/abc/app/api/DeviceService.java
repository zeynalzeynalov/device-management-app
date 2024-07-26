package org.abc.app.api;

import org.abc.app.device.Device;
import org.abc.app.device.DeviceCreateRequest;
import org.abc.app.device.DeviceRepository;
import org.abc.app.device.DeviceUpdateRequest;
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

    public List<Device> getAllFilteredByBrand(String brand) {
        return deviceRepository.findByBrandContaining(brand);
    }

    public Device getById(long deviceId) {
        verifyDeviceId(deviceId);

        return deviceRepository.findById(deviceId).orElseThrow();
    }

    public void create(DeviceCreateRequest request) {
        // TODO: sanitize request fields

        Device device = Device.builder().name(request.getName()).brand(request.getBrand()).build();
        deviceRepository.save(device);
    }

    public void update(long deviceId, DeviceUpdateRequest request) {
        verifyDeviceId(deviceId);

        Device device = deviceRepository.findById(deviceId).orElseThrow();

        // TODO: implement partial update logic
        // TODO: sanitize request fields

        device.setName(request.getName());
        device.setBrand(request.getBrand());

        deviceRepository.save(device);
    }

    public boolean delete(long deviceId) {
        verifyDeviceId(deviceId);

        deviceRepository.deleteById(deviceId);
        return true;
    }

    private void verifyDeviceId(long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalArgumentException(String.format("Device id=%d not found", deviceId));
        }
    }
}
