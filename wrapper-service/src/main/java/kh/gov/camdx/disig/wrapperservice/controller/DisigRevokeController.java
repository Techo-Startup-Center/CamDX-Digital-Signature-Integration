package kh.gov.camdx.disig.wrapperservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kh.gov.camdx.disig.lib.dto.ApiResponse;
import kh.gov.camdx.disig.lib.exceptions.DisigException;
import kh.gov.camdx.disig.lib.service.DigitalSignatureService;
import kh.gov.camdx.disig.wrapperservice.dto.HashRequest;
import kh.gov.camdx.disig.wrapperservice.dto.StringRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1.0/revoke")
@AllArgsConstructor
public class DisigRevokeController extends BaseController {
    private final DigitalSignatureService digitalSignatureService;
    @PostMapping(path = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> signFile(@RequestParam(value = "file") MultipartFile file) throws IOException, DisigException, NoSuchAlgorithmException {
        return getOkResponse(digitalSignatureService.revokeRelayFile(file.getInputStream()));
    }

    @PostMapping("/string")
    public ResponseEntity<ApiResponse> signString(@RequestBody StringRequest request) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return getOkResponse(digitalSignatureService.revokeRelayString(request.getPayloadString()));
    }

    @PostMapping("/hash")
    public ResponseEntity<ApiResponse> signHash(@RequestBody HashRequest request) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return getOkResponse(digitalSignatureService.revokeRelayHash(request.getHashHex()));
    }
}
