package com.cwc.certificate.dto.drive.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FileResponse {
    private String fileName;
    private String downloadUrl;
    private String fileId;
}
