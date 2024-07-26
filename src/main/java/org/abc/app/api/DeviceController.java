package org.abc.app.api;

import org.abc.app.common.RestResponse;
import org.abc.app.device.DeviceCreateUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/devices")
@RestController
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/status")
    public ResponseEntity<RestResponse> checkStatus() {
        return ResponseEntity.ok(new RestResponse("API is running."));
    }

    @GetMapping("/")
    public ResponseEntity<RestResponse> getAll() {
        return ResponseEntity.ok(new RestResponse(deviceService.getAll()));
    }

    @PostMapping("/")
    public ResponseEntity<RestResponse> create(@RequestBody DeviceCreateUpdateRequest request) {
        deviceService.create(request);

        return ResponseEntity.ok(new RestResponse(request));
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<RestResponse> update(@PathVariable long deviceId, @RequestBody DeviceCreateUpdateRequest request) {
        deviceService.update(deviceId, request);

        return ResponseEntity.ok(new RestResponse(request));
    }
}
