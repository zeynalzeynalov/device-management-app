package org.abc.app.api;

import jakarta.validation.Valid;
import org.abc.app.device.Device;
import org.abc.app.device.DeviceCreateRequest;
import org.abc.app.device.DeviceUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeviceService {
    List<Device> getAll();

    List<Device> getAllFilteredByBrand(String brand);

    Device getById(long deviceId);

    @Transactional
    Device create(@Valid DeviceCreateRequest request);

    @Transactional
    void update(long deviceId, @Valid DeviceUpdateRequest request);

    @Transactional
    boolean delete(long deviceId);
}
