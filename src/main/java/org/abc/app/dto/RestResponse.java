package org.abc.app.dto;

import lombok.Data;

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
