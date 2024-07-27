package org.abc.app.service;

import jakarta.validation.Valid;
import org.abc.app.device.Device;
import org.abc.app.dto.DeviceCreateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeviceService {
    List<Device> getAll();

    List<Device> getAllFilteredByBrand(String brand);

    Device getById(long deviceId);

    @Transactional
    Device create(@Valid DeviceCreateRequest request);

    @Transactional
    Device update(long deviceId, @Valid DeviceUpdateRequest request);

    @Transactional
    boolean delete(long deviceId);
}
