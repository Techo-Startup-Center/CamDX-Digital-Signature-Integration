package kh.gov.camdx.disig.wrapperservice.controller;

import kh.gov.camdx.disig.lib.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public class BaseController {
    public BaseController() {
    }

    protected ResponseEntity<ApiResponse> getOkResponse(Object data) {
        return ResponseEntity.ok(new ApiResponse(data));
    }
}
