package kh.gov.camdx.disig.wrapperservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignStringRequest extends SignRequest {
    String payloadString;
}
