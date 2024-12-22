function doGet() {
  const rootFolderId = "1Wx_RVS1WFTqNNIqVFk5pbfwrn8jua3CF";
  const rootFolder = DriveApp.getFolderById(rootFolderId);

  const folderStructure = getFolderStructure(rootFolder);


  const result = JSON.stringify(folderStructure);

  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
}

function getFolderStructure(folder) {
  const folderInfo = {
    folderName: folder.getName(),
    folderId: folder.getId(),
    companies: [],
    users: [],
    files: []
  };

  const subfolders = folder.getFolders();
  while (subfolders.hasNext()) {
    const subfolder = subfolders.next();

    const subfolderInfo = getFolderStructure(subfolder);

    if (folder.getName() === "PDF Documents") {
      folderInfo.companies.push(subfolderInfo);
    } else {
      folderInfo.users.push(subfolderInfo);
    }
  }

  const files = folder.getFiles();
  while (files.hasNext()) {
    const file = files.next();
    folderInfo.files.push({
      fileName: file.getName(),
      downloadUrl: file.getDownloadUrl(),
      fileId: file.getId()
    });
  }

  return folderInfo;
}
