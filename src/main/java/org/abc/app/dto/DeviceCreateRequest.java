package org.abc.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import org.hibernate.validator.constraints.Length;

@Data
@Builder(toBuilder = true)
public class DeviceCreateRequest {

    @NotNull(message = "Device name can not be null")
    @NotBlank(message = "Device name can not be blank")
    @Length(
            min = 3,
            max = 50,
            message = "Device name must have size of between 3 and 50 characters")
    public String name;

    @NotNull(message = "Device brand can not be null")
    @NotBlank(message = "Device brand can not be blank")
    @Length(
            min = 3,
            max = 50,
            message = "Device brand must have size of between 3 and 50 characters")
    public String brand;
}
