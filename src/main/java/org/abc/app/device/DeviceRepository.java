package org.abc.app.device;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBrandContaining(String brand);

    class DeviceNotFoundException extends RuntimeException {

        public DeviceNotFoundException(long deviceId) {
            super(String.format("Device with id=%d not found", deviceId));
        }
    }
}
