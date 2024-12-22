package com.cwc.certificate.model.drive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("fileId")
    private String fileId;
    @JsonProperty("downloadUrl")
    private String downloadUrl;
}
