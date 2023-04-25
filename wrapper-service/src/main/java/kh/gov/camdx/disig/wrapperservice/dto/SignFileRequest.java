package kh.gov.camdx.disig.wrapperservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignFileRequest extends SignRequest {
    MultipartFile file;
}
