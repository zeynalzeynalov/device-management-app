package org.abc.app.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.abc.app.dto.DeviceCreateRequest;
import org.abc.app.dto.RestResponse;
import org.abc.app.service.DeviceService;
import org.abc.app.service.DeviceUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

/** Controller for handling device related API requests. */
@RequestMapping("/api/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * Endpoint to check the API status.
     *
     * @return a message indicating the API is running.
     */
    @GetMapping("/v1/status")
    public ResponseEntity<RestResponse> getApiStatus() {
        return ResponseEntity.ok(new RestResponse("API is running."));
    }

    /**
     * Endpoint to get a list of all devices.
     *
     * @return a list of devices.
     */
    @GetMapping("/v1")
    public ResponseEntity<RestResponse> getAll() {
        return ResponseEntity.ok(new RestResponse(deviceService.getAll()));
    }

    /**
     * Endpoint to get a device by its ID.
     *
     * @param deviceId the ID of the device.
     * @return the device details.
     */
    @GetMapping("/v1/{deviceId}")
    public ResponseEntity<RestResponse> getById(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(new RestResponse(deviceService.getById(deviceId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }

    /**
     * Endpoint to filter devices by brand.
     *
     * @param brand the brand name to filter by.
     * @return a list of devices matching the brand.
     */
    @GetMapping("/v1/filter")
    public ResponseEntity<RestResponse> getAllFilteredByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(new RestResponse(deviceService.getAllFilteredByBrand(brand)));
    }

    /**
     * Endpoint to create a new device.
     *
     * @param request the device creation request details.
     * @param errors any validation errors.
     * @return the created device details.
     */
    @PostMapping("/v1")
    public ResponseEntity<RestResponse> create(
            @Valid @RequestBody DeviceCreateRequest request, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.ok()
                    .body(
                            new RestResponse(
                                    RestResponse.FAIL,
                                    errors.getFieldErrors().get(0).getDefaultMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RestResponse(deviceService.create(request)));
    }

    /**
     * Endpoint to update an existing device.
     *
     * @param deviceId the ID of the device to update.
     * @param request the device update request details.
     * @return the updated device details.
     */
    @PutMapping("/v1/{deviceId}")
    public ResponseEntity<RestResponse> update(
            @PathVariable long deviceId, @RequestBody DeviceUpdateRequest request) {
        try {
            return ResponseEntity.ok(new RestResponse(deviceService.update(deviceId, request)));
        } catch (Exception e) {
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }

    /**
     * Endpoint to delete a device by its ID.
     *
     * @param deviceId the ID of the device to delete.
     * @return a message indicating the result of the deletion.
     */
    @DeleteMapping("/v1/{deviceId}")
    public ResponseEntity<RestResponse> delete(@PathVariable long deviceId) {
        try {
            return ResponseEntity.ok(new RestResponse(deviceService.delete(deviceId)));
        } catch (Exception e) {
            return ResponseEntity.ok(new RestResponse(RestResponse.FAIL, e.getMessage()));
        }
    }
}
