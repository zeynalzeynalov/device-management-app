package org.abc.app.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Builder(toBuilder = true)
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceUpdateRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("brand")
    private String brand;

    public static void sanitizeAndSetFields(DeviceUpdateRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            request.setName(request.getName().trim());
        }
        if (request.getBrand() != null && !request.getBrand().isBlank()) {
            request.setBrand(request.getBrand().trim());
        }
    }
}