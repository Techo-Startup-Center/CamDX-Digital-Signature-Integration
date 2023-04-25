package kh.gov.camdx.disig.lib.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignerResponse {
    String hash;
    String cid;
    List<ApproverDto> signers;
}
