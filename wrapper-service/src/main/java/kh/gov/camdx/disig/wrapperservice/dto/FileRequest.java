package kh.gov.camdx.disig.wrapperservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {
    MultipartFile file;
}
