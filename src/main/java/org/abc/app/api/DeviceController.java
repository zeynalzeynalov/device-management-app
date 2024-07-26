package org.abc.app.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.abc.app.common.RestResponse;
import org.abc.app.device.DeviceCreateRequest;
import org.abc.app.device.DeviceUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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

    @GetMapping
    public ResponseEntity<RestResponse> getAll() {
        return ResponseEntity.ok(new RestResponse(deviceService.getAll()));
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<RestResponse> getById(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(new RestResponse(deviceService.getById(deviceId)));
        }
        catch (Exception e){
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<RestResponse> getAllFilteredByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(new RestResponse(deviceService.getAllFilteredByBrand(brand)));
    }

    @PostMapping
    public ResponseEntity<RestResponse> create(@Valid @RequestBody DeviceCreateRequest request, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.ok().body(new RestResponse(RestResponse.FAIL, errors.getFieldErrors().get(0).getDefaultMessage()));
        }

        deviceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestResponse(request));
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<RestResponse> update(@PathVariable long deviceId, @RequestBody DeviceUpdateRequest request) {
        try {
            deviceService.update(deviceId, request);
            return ResponseEntity.ok(new RestResponse(request));
        }
        catch (Exception e){
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<RestResponse> delete(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(new RestResponse(deviceService.delete(deviceId)));
        }
        catch (Exception e){
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }
}
