package com.cwc.certificate.service.drive;

import com.cwc.certificate.model.drive.Folder;

import java.util.Optional;





public interface GoogleDriveService {

     Folder getFolderStructure() throws Exception;
     Optional<Folder> findFolderByName(Folder folder, String targetFolderName);

}
