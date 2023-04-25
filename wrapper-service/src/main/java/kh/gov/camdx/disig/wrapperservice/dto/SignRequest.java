package kh.gov.camdx.disig.wrapperservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignRequest {
    private String cid;
    private List<String> signers;
}
