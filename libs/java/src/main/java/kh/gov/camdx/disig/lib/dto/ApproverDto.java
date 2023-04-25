package kh.gov.camdx.disig.lib.dto;

import lombok.Data;

@Data
public class ApproverDto {
    String walletAddress;
    int status;
    String statusString;
    CertificateResponse certInfo;
}
