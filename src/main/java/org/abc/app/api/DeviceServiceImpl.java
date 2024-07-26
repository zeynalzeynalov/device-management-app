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
    public void create(@Valid DeviceCreateRequest request) {
        Device device = Device.builder()
                .name(request.getName().substring(0, 50))
                .brand(request.getBrand().substring(0, 50))
                .build();
        deviceRepository.save(device);
    }

    @Transactional
    @Override
    public void update(long deviceId, @Valid DeviceUpdateRequest request) {
        verifyDeviceId(deviceId);

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceRepository.DeviceNotFoundException(deviceId));

        if (request.getName() != null) {
            device.setName(request.getName());
        }
        if (request.getBrand() != null) {
            device.setBrand(request.getBrand());
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
