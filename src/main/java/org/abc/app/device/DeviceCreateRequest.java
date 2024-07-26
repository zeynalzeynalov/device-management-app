package org.abc.app.device;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DeviceCreateRequest {

    @Length(min = 3, max = 50)
    public String name;

    @Length(min = 3, max = 50)
    public String brand;
}
