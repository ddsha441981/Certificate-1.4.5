package com.cwc.certificate.model.drive;


import com.cwc.certificate.model.Company;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Users implements Serializable {
        private static final long serialVersionUID = 1L;
        private String folderName;
        private String folderId;
        private List<Company> companies = new ArrayList<>();
        private List<Users> users = new ArrayList<>();
        private List<DocumentFile> files = new ArrayList<>();
}
