function doGet(e) {
    var jsonData = decodeURIComponent(e.parameter.jsonData);
    var parsedData = JSON.parse(jsonData);

    var template = {
        candidateName: parsedData.candidateName,
        selectedCompany: parsedData.companyName.charAt(0).toUpperCase() + parsedData.companyName.slice(1),
        candidateEmail: parsedData.candidateEmail,
        offerDate: parsedData.offerDate,
        dateOfJoining: parsedData.dateOfJoining,
        jobTitle: parsedData.jobTitle,
        employeeCode: parsedData.employeeCode,
        fixedSalary: parsedData.fixedSalary,
        totalGrossSalary: parsedData.totalGrossSalary,
        basicDA: parsedData.basicDA,
        hra: parsedData.hra,
        specialAllowances: parsedData.specialAllowances,
        performanceBonus: parsedData.performanceBonus,
        finalFixedSalary: parsedData.finalFixedSalary,
        salaryInWord: parsedData.salaryInWord,
        selectedDocumentTypes: parsedData.selectedDocumentTypes
    };

    var documentUrls = template.selectedDocumentTypes;
    var generatedDocuments = [];
    var attachmentBlobs = [];

    var tempFolder = getOrCreateFolderByName("Temp Documents");

    for (var i = 0; i < documentUrls.length; i++) {
        var docUrl = documentUrls[i];
        var updatedUrl = "https://docs.google.com/document/d/" + docUrl + "/edit";
        Logger.log("Attempting to open document with URL: " + updatedUrl);
        try {
            var pdfFolder = getOrCreateFolderByName("PDF Documents");
            var companyFolder = createCompanyFolder(pdfFolder.getId(), template.selectedCompany);
            var candidateFolder = createCandidateFolder(companyFolder, template.candidateName);

            var docFile = DriveApp.getFileById(docUrl);
            var tempCopy = docFile.makeCopy("Temp Copy", tempFolder);
            var tempDocUrl = tempCopy.getId();

            var openFile = DocumentApp.openById(tempDocUrl);
            var body = openFile.getBody();
            body.replaceText("{Name}", template.candidateName);
            body.replaceText("{CompanyName}", template.selectedCompany);
            body.replaceText("{OfferDate}", template.offerDate);
            body.replaceText("{JoiningDate}", template.dateOfJoining);
            body.replaceText("{JobTitle}", template.jobTitle);
            body.replaceText("{EmployeeCode}", template.employeeCode);
            body.replaceText("{SalaryInWord}", template.salaryInWord);
            body.replaceText("FixedSalary", template.fixedSalary);
            body.replaceText("TotalGrossSalary", template.totalGrossSalary);
            body.replaceText("BasicDA", template.basicDA);
            body.replaceText("Hra", template.hra);
            body.replaceText("SpecialAllowances", template.specialAllowances);
            body.replaceText("PerformanceBonus", template.performanceBonus);
            body.replaceText("FinalFixedSalary", template.finalFixedSalary);
            

            openFile.saveAndClose();

            var blob = openFile.getAs('application/pdf').setName("Document_" + template.dateOfJoining + ".pdf");

            DriveApp.createFile(blob).moveTo(candidateFolder);

            generatedDocuments.push({
                url: tempDocUrl,
                candidateName: template.candidateName,
                offerDate: template.offerDate,
                dateOfJoining: template.dateOfJoining,
                jobTitle: template.jobTitle,
                employeeCode: template.employeeCode
            });
            attachmentBlobs.push(blob);
            Utilities.sleep(2000);
        } catch (e) {
            Logger.log("Error accessing document: " + e.message);
            generatedDocuments.push({
                error: "Error accessing document",
                documentUrl: docUrl
            });
        }
    }

    deleteTempFiles(tempFolder);

    sendEmail(template, attachmentBlobs);

    return ContentService.createTextOutput(JSON.stringify(generatedDocuments))
        .setMimeType(ContentService.MimeType.JSON);
}

//Send Mail
function sendEmail(template, attachmentBlobs) {
    var emailBody = {
        to: template.candidateEmail,
        subject: "Documents",
        body: "Hello " + template.candidateName + ",\n\nGreetings! We hope this message finds you well.\n\nWe are pleased to inform you that the following documents are now ready for your review:\n \nPlease find the attached documents.\n\nIf you have any questions or need further assistance, feel free to contact us.\n\nBest regards,\nThe [" + template.selectedCompany + "] Team",
        attachments: attachmentBlobs
    };

    MailApp.sendEmail(emailBody);
}

//Company Folder
function createCompanyFolder(parentFolderId, companyName) {
    var folderIterator = DriveApp.getFolderById(parentFolderId).getFoldersByName(companyName);
    if (folderIterator.hasNext()) {
        return folderIterator.next();
    } else {
        return DriveApp.getFolderById(parentFolderId).createFolder(companyName);
    }
}

//Candidate Name Folder
function createCandidateFolder(parentFolder, candidateName) {
    var folderIterator = parentFolder.getFoldersByName(candidateName);
    if (folderIterator.hasNext()) {
        return folderIterator.next();
    } else {
        return parentFolder.createFolder(candidateName);
    }
}

// Delete Temp files after updating docs files
function deleteTempFiles(folder) {
    var files = folder.getFiles();
    while (files.hasNext()) {
        var file = files.next();
        file.setTrashed(true);
    }
}

//Create Pdf and Temp Folder
function getOrCreateFolderByName(folderName) {
    var folderIterator = DriveApp.getFoldersByName(folderName);
    if (folderIterator.hasNext()) {
        return folderIterator.next();
    } else {
        return DriveApp.createFolder(folderName);
    }
}
