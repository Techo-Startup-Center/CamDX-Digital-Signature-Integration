package kh.gov.camdx.disig.wrapperservice.controller;

import kh.gov.camdx.disig.lib.service.DigitalSignatureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/approve")
@AllArgsConstructor
public class DisigApproveController {
    private final DigitalSignatureService digitalSignatureService;


}
