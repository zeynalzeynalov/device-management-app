package org.abc.app.api;

import lombok.RequiredArgsConstructor;
import org.abc.app.common.RestResponse;
import org.abc.app.device.DeviceCreateUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/status")
    public ResponseEntity<RestResponse> checkStatus() {
        return ResponseEntity.ok(new RestResponse("API is running."));
    }

    @GetMapping("/")
    public ResponseEntity<RestResponse> getAll() {
        return ResponseEntity.ok(new RestResponse(deviceService.getAll()));
    }

    @GetMapping(value = "/{deviceId}")
    public ResponseEntity<RestResponse> getById(@PathVariable long deviceId) {
        return ResponseEntity.ok(new RestResponse(deviceService.getById(deviceId)));
    }

    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<RestResponse> getAllFilteredByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(new RestResponse(deviceService.getAllFilteredByBrand(brand)));
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

    @DeleteMapping(path = "/{deviceId}")
    public ResponseEntity<RestResponse> delete(@PathVariable long deviceId) {
        return ResponseEntity.ok(new RestResponse(deviceService.delete(deviceId)));
    }
}
