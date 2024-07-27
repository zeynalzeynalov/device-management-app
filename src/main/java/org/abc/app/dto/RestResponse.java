package org.abc.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RestResponse {

    // Constants representing dto response statuses
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    // Response status
    private final String status;

    // Response data
    private final Object data;

    public RestResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public RestResponse(Object data) {
        this.status = SUCCESS;
        this.data = data;
    }

    public RestResponse() {
        this.status = SUCCESS;
        this.data = null;
    }
}
