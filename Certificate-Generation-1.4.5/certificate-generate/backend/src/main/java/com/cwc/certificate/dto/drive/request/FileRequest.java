package com.cwc.certificate.dto.drive.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FileRequest {
    private String fileName;
    private String downloadUrl;
    private String fileId;
}
