package org.abc.app.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.abc.app.device.Device;
import org.abc.app.repository.DeviceRepository;
import org.abc.app.dto.DeviceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    // Jackson object mapper for converting objects to JSON
    private final ObjectMapper objectMapper;

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
    public Device update(long deviceId, @Valid DeviceUpdateRequest request) {
        verifyDeviceId(deviceId);

        DeviceUpdateRequest.sanitizeAndSetFields(request);

        Device device = deviceRepository.findById(deviceId).get();

        try {
            objectMapper.updateValue(device, request);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }

        deviceRepository.save(device);

        return device;
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
