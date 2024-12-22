package com.cwc.certificate.model.drive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Folder implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("folderName")
    private String folderName;
    @JsonProperty("folderId")
    private String folderId;
    @JsonProperty("companies")
    private List<Folder> companies = new ArrayList<>();
    @JsonProperty("users")
    private List<Folder> users = new ArrayList<>();
    @JsonProperty("files")
    private List<DocumentFile> files = new ArrayList<>();







}
