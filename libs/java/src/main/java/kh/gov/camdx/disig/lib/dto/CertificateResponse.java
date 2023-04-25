package kh.gov.camdx.disig.lib.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CertificateResponse {
    String serialNumber;
    String subject;
    String commonName;
    String org;
    String orgUnit;
    String country;
    String email;
    String pubKey;
    Timestamp notBefore;
    Timestamp notAfter;
    String certificateChain;
}
