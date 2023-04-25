package kh.gov.camdx.disig.wrapperservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignHashRequest extends SignRequest {
    String hashHex;
}
