package org.abc.app.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.abc.app.device.Device;
import org.abc.app.dto.DeviceCreateRequest;
import org.abc.app.repository.DeviceRepository;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link DeviceService} interface. Provides methods for managing devices in
 * the application.
 */
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    // Repository for accessing device data
    private final DeviceRepository deviceRepository;

    // Jackson object mapper for converting objects to/from JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retrieves all devices.
     *
     * @return a list of all devices.
     */
    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    /**
     * Retrieves devices filtered by the specified brand.
     *
     * @param brand the brand to filter by.
     * @return a list of devices matching the brand filter.
     */
    @Override
    public List<Device> getAllFilteredByBrand(String brand) {
        return deviceRepository.findByBrandContaining(brand);
    }

    /**
     * Retrieves a device by its ID.
     *
     * @param deviceId the ID of the device.
     * @return the device with the specified ID.
     * @throws DeviceNotFoundException if the device is not found.
     */
    @Override
    public Device getById(long deviceId) {
        verifyDeviceId(deviceId);
        return deviceRepository
                .findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    /**
     * Creates a new device.
     *
     * @param request the device creation request.
     * @return the created device.
     */
    @Transactional
    @Override
    public Device create(@Valid DeviceCreateRequest request) {
        Device device =
                Device.builder()
                        .name(request.getName().trim())
                        .brand(request.getBrand().trim())
                        .build();
        return deviceRepository.save(device);
    }

    /**
     * Updates an existing device.
     *
     * @param deviceId the ID of the device to update.
     * @param request the device update request.
     * @return the updated device.
     * @throws DeviceNotFoundException if the device is not found.
     */
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
        return deviceRepository.save(device);
    }

    /**
     * Deletes a device by its ID.
     *
     * @param deviceId the ID of the device to delete.
     * @return true if the device was deleted, false otherwise.
     */
    @Transactional
    @Override
    public boolean delete(long deviceId) {
        verifyDeviceId(deviceId);
        deviceRepository.deleteById(deviceId);
        return true;
    }

    /**
     * Verifies if a device exists by its ID.
     *
     * @param deviceId the ID of the device.
     * @throws DeviceNotFoundException if the device is not found.
     */
    private void verifyDeviceId(long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new DeviceNotFoundException(deviceId);
        }
    }

    /**
     * Sets a custom {@link ObjectMapper}, useful for testing.
     *
     * @param objectMapper the custom object mapper to set.
     */
    @VisibleForTesting
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
