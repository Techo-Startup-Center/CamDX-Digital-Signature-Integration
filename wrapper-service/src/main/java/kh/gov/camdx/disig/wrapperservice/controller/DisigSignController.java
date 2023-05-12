package kh.gov.camdx.disig.wrapperservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kh.gov.camdx.disig.lib.dto.ApiResponse;
import kh.gov.camdx.disig.lib.exceptions.DisigException;
import kh.gov.camdx.disig.lib.service.DigitalSignatureService;
import kh.gov.camdx.disig.wrapperservice.dto.SignFileRequest;
import kh.gov.camdx.disig.wrapperservice.dto.SignHashRequest;
import kh.gov.camdx.disig.wrapperservice.dto.SignStringRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/sign")
@AllArgsConstructor
public class DisigSignController extends BaseController {
    private final DigitalSignatureService digitalSignatureService;

    @PostMapping(path = "/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> signFile(@RequestParam(value = "cid", required = false) String cid, @RequestParam(value = "signers") List<String> signers, @RequestParam(value = "file") MultipartFile file) throws IOException, DisigException, NoSuchAlgorithmException {
        return getOkResponse(digitalSignatureService.signRelayFile(file.getInputStream(), cid, signers));
    }

    @PostMapping("/string")
    public ResponseEntity<ApiResponse> signString(@RequestBody SignStringRequest request) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return getOkResponse(digitalSignatureService.signRelayString(request.getPayloadString(), request.getCid(), request.getSigners()));
    }

    @PostMapping("/hash")
    public ResponseEntity<ApiResponse> signHash(@RequestBody SignHashRequest request) throws DisigException, NoSuchAlgorithmException, JsonProcessingException {
        return getOkResponse(digitalSignatureService.signRelayHash(request.getHashHex(), request.getCid(), request.getSigners()));
    }
}
