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
        salaryExpose: parsedData.salaryExpose,
        salaryInWord: parsedData.salaryInWord,
        selectedDocumentTypes: parsedData.selectedDocumentTypes,
        bank: parsedData.bank,
        identificationDetails: parsedData.identificationDetails,
        companyData : parsedData.company
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

            //Document Details
            body.replaceText("{Name}", template.candidateName);
            body.replaceText("{CompanyName}", template.selectedCompany);
            body.replaceText("{OfferDate}", template.offerDate);
            body.replaceText("{JoiningDate}", template.dateOfJoining);
            body.replaceText("{JobTitle}", template.jobTitle);
            body.replaceText("{EmployeeCode}", template.employeeCode);
            body.replaceText("{SalaryInWord}", template.salaryInWord);
            //Salary Expose
            body.replaceText("Ctc", template.salaryExpose.ctc);
            body.replaceText("Epf", template.salaryExpose.epf);
            body.replaceText("Gratuitys", template.salaryExpose.gratuity);
            body.replaceText("Hra", template.salaryExpose.hra);
            body.replaceText("ProfTax", template.salaryExpose.profTax);
            body.replaceText("InTax", template.salaryExpose.incomeTax);
            body.replaceText("MediInsu", template.salaryExpose.medicalInsurance);
            body.replaceText("Invest80C", template.salaryExpose.investments80C);
            body.replaceText("PerfBonus", template.salaryExpose.performanceBonus);
            body.replaceText("GrossSalary", template.salaryExpose.grossSalary);
            body.replaceText("InHSalary", template.salaryExpose.inHandSalary);
            body.replaceText("MonGrSalary", template.salaryExpose.monthlyGrossSalary);
            body.replaceText("MonInHSalary", template.salaryExpose.monthlyInHandSalary);
            // Company Details
            body.replaceText("{CompanyEmail}", template.companyData.companyEmail);
            body.replaceText("{CompanyPhone}", template.companyData.companyPhone);
            body.replaceText("{CompanyWebsite}", template.companyData.companyWebsite);
            // Address Details
            body.replaceText("{Country}", template.companyData.addresses[0].country);
            body.replaceText("{ZipCode}", template.companyData.addresses[0].zipCode);
            body.replaceText("{BuildingNumber}", template.companyData.addresses[0].buildingNumber);
            body.replaceText("{City}", template.companyData.addresses[0].city);
            body.replaceText("{Street}", template.companyData.addresses[0].street);
            body.replaceText("{Landmark}", template.companyData.addresses[0].landmark);
            body.replaceText("{AddressType}", template.companyData.addresses[0].addressType);
            // HR Details
            body.replaceText("{HrName}", template.companyData.hrDetails.hrName);
            body.replaceText("{HrContactNumber}", template.companyData.hrDetails.hrContactNumber);
            body.replaceText("{HrEmail}", template.companyData.hrDetails.hrEmail);
            // Manager Details
            body.replaceText("{ManagerName}", template.companyData.managerDetails.managerName);
            body.replaceText("{ManagerContactNumber}", template.companyData.managerDetails.managerContactNumber);
            body.replaceText("{ManagerEmail}", template.companyData.managerDetails.managerEmail);
            // Other Company Information
            body.replaceText("{CompanyRegistrationNumber}", template.companyData.companyRegistrationNumber);
            body.replaceText("{CompanyDomainType}", template.companyData.companyDomainType);
            body.replaceText("{IndustryType}", template.companyData.industryType);
            body.replaceText("{YearOfEstablishment}", template.companyData.yearOfEstablishment);
            body.replaceText("{CompanySize}", template.companyData.companySize);
            body.replaceText("{CompanyFounder}", template.companyData.companyFounder);
            body.replaceText("{CompanyRevenue}", template.companyData.companyRevenue);
            body.replaceText("{CompanyLicenseNumber}", template.companyData.companyLicenseNumber);
            //Bank Details
            body.replaceText("{BankName}",template.bank.bankName);
            body.replaceText("{IfscCode}",template.bank.ifscCode);
            body.replaceText("{AccountNo}",template.bank.accountNo);
            body.replaceText("{AccountHolderName}",template.bank.accountHolderName);
            body.replaceText("{UanNumber}",template.bank.uanNumber);
            body.replaceText("{EsiNumber}",template.bank.esiNumber);
            //Identification Details
            body.replaceText("{AadarNumber}",template.identificationDetails.aadarNumber);
            body.replaceText("{PanNumber}",template.identificationDetails.panNumber);

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

    //delete after creating docs
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




