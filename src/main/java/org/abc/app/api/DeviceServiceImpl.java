package org.abc.app.api;

import jakarta.validation.Valid;
import org.abc.app.device.Device;
import org.abc.app.device.DeviceCreateRequest;
import org.abc.app.device.DeviceRepository;
import org.abc.app.device.DeviceUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getAllFilteredByBrand(String brand) {
        return deviceRepository.findByBrandContaining(brand);
    }

    @Override
    public Device getById(long deviceId) {
        verifyDeviceId(deviceId);
        return deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceRepository.DeviceNotFoundException(deviceId));
    }

    @Transactional
    @Override
    public Device create(@Valid DeviceCreateRequest request) {
        Device device = Device.builder().name(request.getName().trim()).brand(request.getBrand().trim()).build();
        deviceRepository.save(device);

        return device;
    }

    @Transactional
    @Override
    public void update(long deviceId, @Valid DeviceUpdateRequest request) {
        verifyDeviceId(deviceId);

        Device device = deviceRepository.findById(deviceId).get();

        if (request.getName() != null && !request.getName().isBlank()) {
            device.setName(request.getName().trim());
        }
        if (request.getBrand() != null && !request.getBrand().isBlank()) {
            device.setBrand(request.getBrand().trim());
        }

        deviceRepository.save(device);
    }

    @Transactional
    @Override
    public boolean delete(long deviceId) {
        verifyDeviceId(deviceId);
        deviceRepository.deleteById(deviceId);
        return true;
    }

    private void verifyDeviceId(long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new DeviceRepository.DeviceNotFoundException(deviceId);
        }
    }
}
