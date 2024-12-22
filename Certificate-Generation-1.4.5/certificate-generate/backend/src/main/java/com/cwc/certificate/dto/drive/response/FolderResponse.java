package com.cwc.certificate.dto.drive.response;

import com.cwc.certificate.model.drive.Folder;
import lombok.*;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FolderResponse {
    private String folderName;
    private String folderId;
    private List<File> files;
    private List<Folder> subfolders;
    private List<Folder> companies;
    private List<Folder> users;
}
