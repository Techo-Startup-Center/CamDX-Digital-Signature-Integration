package kh.gov.camdx.disig.lib.dto;

import lombok.Data;

@Data
public class ApiResponse {
    protected Integer error;
    protected String message;
    protected Object data;

    public ApiResponse() {
        this.error = 0;
        this.message = "Successfully";
        this.data = null;
    }

    public ApiResponse(Object data) {
        this.error = 0;
        this.message = "Successfully";
        this.data = data;
    }

    public ApiResponse(Integer error, String message) {
        this.error = error;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(Integer error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }
}
