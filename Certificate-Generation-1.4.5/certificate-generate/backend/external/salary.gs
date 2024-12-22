function doGet(e) {
    try {
        Logger.log("Event object: " + JSON.stringify(e));

        var jsonData = decodeURIComponent(e.parameter.responseJson);
        Logger.log("Received JSON data: " + jsonData);


        var parsedData = JSON.parse(jsonData);


        var extractedData = {
            candidateName: parsedData.certificate.candidateName,
            selectedCompany: parsedData.certificate.companyName,
            candidateEmail: parsedData.certificate.candidateEmail,
            offerDate: parsedData.certificate.offerDate,
            dateOfJoining: parsedData.certificate.dateOfJoining,
            jobTitle: parsedData.certificate.jobTitle,
            employeeCode: parsedData.certificate.employeeCode,
            companyName: parsedData.certificate.companyName,
            changeStatus: parsedData.certificate.changeStatus,
            salaryInWord: parsedData.certificate.salaryInWord,
            status: parsedData.certificate.status,
            deleted: parsedData.certificate.deleted,
            createdAt: parsedData.certificate.createdAt,
            updatedAt: parsedData.certificate.updatedAt,
            selectedDocumentTypes: parsedData.certificate.selectedDocumentTypes,
            salaryFrom: parsedData.salaryFrom,
            salaryTo: parsedData.salaryTo,
            salaryMode: parsedData.salaryMode,
            selectedDocumentType: parsedData.selectedDocumentType,
            salaryMonths: generateSalaryMonths(parsedData.salaryFrom, parsedData.salaryTo),
            //Salary
             salaryExpose:parsedData.certificate.salaryExpose,

            //Bank
            // bank: parsedData.bank,
             bank: parsedData.certificate.bank,
            //Identification
             identificationDetails: parsedData.certificate.identificationDetails
            // identificationDetails: parsedData.identificationDetails
        };

        paymentSlip(extractedData);

        return ContentService.createTextOutput({ message: "Pay slip generated successfully."})
            .setMimeType(ContentService.MimeType.TEXT);

    } catch (error) {
        Logger.log("Error parsing JSON data: " + error);
        return ContentService.createTextOutput(JSON.stringify({ error: "Error parsing JSON data: " + error }))
            .setMimeType(ContentService.MimeType.JSON);
    }
}

function generateSalaryMonths(salaryFrom, salaryTo) {
    var startDate = new Date(salaryFrom);
    var endDate = new Date(salaryTo);
    var salaryMonths = [];

    while (startDate <= endDate) {
        var formattedMonth = Utilities.formatDate(startDate, Session.getScriptTimeZone(), "MMMM yyyy");
        salaryMonths.push(formattedMonth);
        startDate.setMonth(startDate.getMonth() + 1);
    }

    return salaryMonths;
}

function paymentSlip(template) {
    let docName = "Pay Slip";
    let docFileId = template.selectedDocumentType;
    let pdfParentFolderName = "PDF Folder";
    let companyName = template.companyName;
    let tempFolderName = "Temp";


    let pdfParentFolder = DriveApp.getFoldersByName(pdfParentFolderName);
    if (!pdfParentFolder.hasNext()) {
        pdfParentFolder = DriveApp.createFolder(pdfParentFolderName);
    } else {
        pdfParentFolder = pdfParentFolder.next();
    }


    let companyFolder = pdfParentFolder.getFoldersByName(companyName);
    if (!companyFolder.hasNext()) {
        companyFolder = pdfParentFolder.createFolder(companyName);
    } else {
        companyFolder = companyFolder.next();
    }


    let tempFolder = companyFolder.getFoldersByName(tempFolderName);
    if (!tempFolder.hasNext()) {
        tempFolder = companyFolder.createFolder(tempFolderName);
    } else {
        tempFolder = tempFolder.next();
    }

    var startDate = new Date(template.salaryFrom);
    var endDate = new Date(template.salaryTo);
    var payslips = [];

    while (startDate <= endDate) {
        let formattedMonth = Utilities.formatDate(startDate, Session.getScriptTimeZone(), "MMMM yyyy");
        let payslipName = docName + "_" + template.candidateName + "_" + formattedMonth;
        let dummyFile = DriveApp.getFileById(docFileId).makeCopy(tempFolder).setName(payslipName);
        let openFile = DocumentApp.openById(dummyFile.getId());
        let body = openFile.getBody();

        body.replaceText("{Name}", template.candidateName);
        body.replaceText("{JobTitle}", template.jobTitle);


        openFile.saveAndClose();

        let blob = dummyFile.getAs('application/pdf').setName(payslipName + ".pdf");

        DriveApp.createFile(blob).moveTo(companyFolder);
        payslips.push(blob);
        startDate.setMonth(startDate.getMonth() + 1);
    }
    return payslips;
}

function createFolders(parentFolderName, folderNames) {
    let parentFolder = DriveApp.getFoldersByName(parentFolderName);
    if (!parentFolder.hasNext()) {
        parentFolder = DriveApp.createFolder(parentFolderName);
    } else {
        parentFolder = parentFolder.next();
    }

    let folders = [];
    for (let name of folderNames) {
        let folder = parentFolder.getFoldersByName(name);
        if (!folder.hasNext()) {
            folder = parentFolder.createFolder(name);
        } else {
            folder = folder.next();
        }
        folders.push(folder);
    }

    return folders;
}
function createFolders(parentFolderName, folderNames) {
    let parentFolder = DriveApp.getFoldersByName(parentFolderName);
    if (!parentFolder.hasNext()) {
        parentFolder = DriveApp.createFolder(parentFolderName);
    } else {
        parentFolder = parentFolder.next();
    }

    let folders = [];
    for (let name of folderNames) {
        let folder = parentFolder.getFoldersByName(name);
        if (!folder.hasNext()) {
            folder = parentFolder.createFolder(name);
        } else {
            folder = folder.next();
        }
        folders.push(folder);
    }

    return folders;
}

function deleteFolderContents(folder) {
    let files = folder.getFiles();
    while (files.hasNext()) {
        let file = files.next();
        DriveApp.getFileById(file.getId()).setTrashed(true);
    }
}

function paymentSlip(template) {
    let docName = "Pay Slip";
    let docFileId = template.selectedDocumentType;
    let tempFolderName = "temp";
    let pdfFolderName = "pdf";

    let tempFolders = DriveApp.getFoldersByName(tempFolderName);
    let tempFolder;
    if (tempFolders.hasNext()) {
        tempFolder = tempFolders.next();
    } else {
        tempFolder = DriveApp.createFolder(tempFolderName);
    }

    let pdfFolders = DriveApp.getFoldersByName(pdfFolderName);
    let pdfFolder;
    if (pdfFolders.hasNext()) {
        pdfFolder = pdfFolders.next();
    } else {
        pdfFolder = DriveApp.createFolder(pdfFolderName);
    }

    let timestamp = new Date().getTime().toString();
    let tempSubFolder = tempFolder.createFolder(timestamp);

    var startDate = new Date(template.salaryFrom);
    var endDate = new Date(template.salaryTo);
    var payslips = [];

    while (startDate <= endDate) {
        let formattedMonth = Utilities.formatDate(startDate, Session.getScriptTimeZone(), "MMMM yyyy");
        let payslipName = docName + "_" + template.candidateName + "_" + formattedMonth;


        let companyFolders = pdfFolder.getFoldersByName(template.companyName);
        let companyFolder;
        if (companyFolders.hasNext()) {
            companyFolder = companyFolders.next();
        } else {
            companyFolder = pdfFolder.createFolder(template.companyName);
        }

        let candidateFolders = companyFolder.getFoldersByName(template.candidateName);
        let candidateFolder;
        if (candidateFolders.hasNext()) {
            candidateFolder = candidateFolders.next();
        } else {
            candidateFolder = companyFolder.createFolder(template.candidateName);
        }

        let dummyFile = DriveApp.getFileById(docFileId).makeCopy(tempSubFolder);
        dummyFile.setName(payslipName);
        let openFile = DocumentApp.openById(dummyFile.getId());
        let body = openFile.getBody();

            body.replaceText("{Name}", template.candidateName);
            body.replaceText("{SalaryMonths}", formattedMonth);
            body.replaceText("{CompanyName}", template.selectedCompany);
            body.replaceText("{OfferDate}", template.offerDate);
            body.replaceText("{JoiningDate}", template.dateOfJoining);
            body.replaceText("{JobTitle}", template.jobTitle);
            body.replaceText("{EmployeeCode}", template.employeeCode);
            body.replaceText("{SalaryInWord}", template.salaryInWord);
            //Salary Expose
            body.replaceText("CTC", template.salaryExpose.ctc);
            body.replaceText("EPF", template.salaryExpose.epf);
            body.replaceText("Gratuity", template.salaryExpose.gratuity);
            body.replaceText("HRA", template.salaryExpose.hra);
            body.replaceText("ProfTax", template.salaryExpose.profTax);
            body.replaceText("InTax", template.salaryExpose.incomeTax);
            body.replaceText("MediInsu", template.salaryExpose.medicalInsurance);
            body.replaceText("Invest80C", template.salaryExpose.investments80C);
            body.replaceText("PerfBonus", template.salaryExpose.performanceBonus);
            body.replaceText("GrossSalary", template.salaryExpose.grossSalary);
            body.replaceText("InHSalary", template.salaryExpose.inHandSalary);
            body.replaceText("MonGrSalary", template.salaryExpose.monthlyGrossSalary);
            body.replaceText("MonInHSalary", template.salaryExpose.monthlyInHandSalary);

            //Bank details

            // body.replaceText("FixedSalary", template.fixedSalary);
            // body.replaceText("TotalGrossSalary", template.totalGrossSalary);
            // body.replaceText("BasicDA", template.basicDA);
            // body.replaceText("Hra", template.hra);
            // body.replaceText("SpecialAllowances", template.specialAllowances);
            // body.replaceText("PerformanceBonus", template.performanceBonus);
            // body.replaceText("FinalFixedSalary", template.finalFixedSalary);
            //Bank Details

           //Identification Details
            body.replaceText("{PanNumber}",template.identificationDetails.panNumber);

        openFile.saveAndClose();

        let blob = dummyFile.getAs('application/pdf').setName(payslipName + ".pdf");

        let payslipFile = DriveApp.createFile(blob);
        payslipFile.moveTo(candidateFolder);

        payslips.push(payslipFile);
        startDate.setMonth(startDate.getMonth() + 1);
    }
    return payslips;
}