package com.cwc.certificate.controller;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.CertificateRequest;
import com.cwc.certificate.dto.request.MessageRequest;
import com.cwc.certificate.dto.response.CertificatePaginationResponse;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.dto.response.CompanyResponse;
import com.cwc.certificate.dto.response.GenderCountResponse;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.IdentificationDetails;
import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.service.DocumentService;
import com.cwc.certificate.service.JasperReportService;
import com.cwc.certificate.utility.DateUtils;
import com.cwc.certificate.utility.FileValidator;
import com.cwc.certificate.utility.NumberToWordsConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */


@RestController
@RequestMapping("/api/v1/certificate")
@CrossOrigin("*")
@Slf4j
@Tag(name = "Certificate", description = "Certificate management API")
public class CertificateController {


    private final DocumentService documentService;
    private final CertificateService certificateService;
    private final JasperReportService jasperReportService;
    private final NumberToWordsConverter numberToWordsConverter;
    private final FileValidator fileValidator;
    private final ObjectMapper objectMapper;

    @Autowired
    public CertificateController(DocumentService documentService, CertificateService certificateService, JasperReportService jasperReportService, NumberToWordsConverter numberToWordsConverter, FileValidator fileValidator, ObjectMapper objectMapper) {
        this.documentService = documentService;
        this.certificateService = certificateService;
        this.jasperReportService = jasperReportService;
        this.numberToWordsConverter = numberToWordsConverter;
        this.fileValidator = fileValidator;
        this.objectMapper = objectMapper;
    }



    @Operation(summary = "Create a new Certificate")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })

    @PostMapping("/add/file")
    public ResponseEntity<String> addCertificate(
            @RequestParam("docs") MultipartFile docs,
            @RequestParam("formData") String formData) {
        try {
            if (!fileValidator.isPngFile(docs)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Only PNG files are allowed for logo and signature.");
            }

            //Form data convert into object
            CertificateRequest certificateRequest = objectMapper.readValue(formData, CertificateRequest.class);


//            Optional Json
//            if (certificateRequest.getBank() == null) {
//               Bank bank = Bank.builder()
//
//                       .bankName(ConstantValue.DEFAULT_BANK_NAME)
//                       .ifscCode(ConstantValue.DEFAULT_IFSC_CODE)
//                       .accountHolderName(ConstantValue.DEFAULT_ACCOUNT_HOLDER_NAME)
//                       .accountNo(ConstantValue.DEFAULT_ACCOUNT_NUMBER)
//                       .uanNumber(ConstantValue.DEFAULT_UAN)
//                       .esiNumber(ConstantValue.DEFAULT_ESI)
//                       .customerId(ConstantValue.DEFAULT_CUSTOMERID)
//                       .build();
//               certificateRequest.setBank(bank);
//            }
//
//            if (certificateRequest.getIdentificationDetails() == null) {
//                IdentificationDetails identificationDetails =  IdentificationDetails.builder()
//                      .aadarNumber(ConstantValue.DEFAULT_AADAR_NUMBER)
//                      .panNumber(ConstantValue.DEFAULT_PAN_NUMBER)
//                      .docName(ConstantValue.DEFAULT_DOC_NAME)
//                      .documentType(ConstantValue.DEFAULT_DOCUMENT_TYPE)
//                      .documentData(ConstantValue.DEFAULT_DOCUMENT_DATA)
//                      .build();
//              certificateRequest.setIdentificationDetails(identificationDetails);
//            }

            log.info("Received formData: {}", formData);

            log.info("Received documentType file: {}", docs.getOriginalFilename());

            Optional<String> originalFilename = Optional.ofNullable(docs.getOriginalFilename());
            String docsOriginalFilename = originalFilename.map(StringUtils::cleanPath).orElse(null);
//            String docsOriginalFilename = StringUtils.cleanPath(docs.getOriginalFilename()); //Raise Null

            String aadarNumber = certificateRequest.getIdentificationDetails().getAadarNumber();
            String panNumber = certificateRequest.getIdentificationDetails().getPanNumber();
            IdentificationDetails identificationDetails = new IdentificationDetails();
            identificationDetails.setAadarNumber(aadarNumber);
            identificationDetails.setPanNumber(panNumber);
            identificationDetails.setDocumentType(certificateRequest.getIdentificationDetails().getDocumentType());
            identificationDetails.setDocName(docsOriginalFilename);


            Optional<String> filenameOptional = Optional.ofNullable(docsOriginalFilename);
            if (filenameOptional.isPresent() && filenameOptional.get().contains("..")) {
                log.error("Invalid file name: {}", filenameOptional.get());
                throw new Exception("Invalid file name: " + filenameOptional.get());
            }

            try {
                identificationDetails.setDocumentData(Base64.getEncoder().encodeToString(docs.getBytes()));
                certificateRequest.setIdentificationDetails(identificationDetails);

            } catch (IOException e) {
                log.error("Error processing request:", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process the request. Please try again.");
            }

            log.info("Uploaded file saved successfully. Paths: DocType={}", docsOriginalFilename);


            String dateOfJoining = certificateRequest.getDateOfJoining();
            String offerDate = certificateRequest.getOfferDate();

            String s1 = DateUtils.convertDateFormat(dateOfJoining);
            String s2 = DateUtils.convertDateFormat(offerDate);

            certificateRequest.setDateOfJoining(s1);
            certificateRequest.setOfferDate(s2);

            log.info("Certificate request: {}", certificateRequest);

            //List<Documents> documentsList = certificateRequest.getSelectedDocumentTypes().stream().map(documents -> documents).collect(Collectors.toList());

            //salary convert into number to word
            log.info("Fixed salary before convert into word : {}", certificateRequest.getSalaryExpose().getInHandSalary());
            double fixedSalary = certificateRequest.getSalaryExpose().getInHandSalary();
            String afterConverted = numberToWordsConverter.convert((int) fixedSalary);
            certificateRequest.setSalaryInWord(afterConverted);
            log.info("Fixed salary after convert into word : {}", afterConverted);

            //Company Name always store uppercase
            String companyName = certificateRequest.getCompanyName().toUpperCase();
            certificateRequest.setCompanyName(companyName);

            // call database here
            this.certificateService.addCertificateData(certificateRequest);

            return new ResponseEntity<>("Certificate and document saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing request:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the request. Please try again.");
        }
    }

    @Operation(summary = "Before register candidate check email")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        boolean candidateEmail = this.certificateService.checkCandidateEmail(request);
        return ResponseEntity.ok(Map.of("emailExists", candidateEmail));
    }

    @Operation(summary = "Update certificate information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @PutMapping("/update/{certificateId}")
    public ResponseEntity<Certificate> updateCertificate(@RequestBody CertificateRequest certificateRequest, @PathVariable("certificateId") Integer certificateId) {
        Certificate updateCertificateData = this.certificateService.updateCertificateData(certificateRequest, certificateId);
        return new ResponseEntity<>(updateCertificateData, HttpStatus.OK);
    }

    @Operation(summary = "Delete certificate", description = "A JWT token is required to access this API...")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "206", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @DeleteMapping("/{certificateId}")
    public MessageRequest deleteCertificate(@PathVariable("certificateId") Integer certificateId) {
        this.certificateService.deleteCertificate(certificateId);
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Certificate deleted successfully with id: {} " + certificateId)
                .build();
        return new ResponseEntity<>(messageRequest, HttpStatus.NO_CONTENT).getBody();
    }

    @Operation(summary = "Retrieve all certificates list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RateLimited
    @GetMapping("/all")
    public ResponseEntity<List<CertificateResponse>> getAllCerificate() {
        List<CertificateResponse> certificateResponseList = this.certificateService.allCertificates();
        return new ResponseEntity<>(certificateResponseList, HttpStatus.OK);
    }


    @Operation(summary = "Retrieve all certificates list by Pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RateLimited
    @GetMapping("/pagination/all")
    public ResponseEntity<List<CertificatePaginationResponse>> getAllCertificateByPagination(
            @RequestParam(value = "pageNumber", defaultValue = ConstantValue.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = ConstantValue.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantValue.DEFAULT_SORT_BY_CERTIFICATE, required = false) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = ConstantValue.DEFAULT_SORT_DIR, required = false) String sortOrder
    ) {
        List<CertificatePaginationResponse> certificateListByPagination = this.certificateService.getCertificateListByPagination(pageNumber, pageSize, sortBy, sortOrder);
        if (certificateListByPagination.isEmpty()) {
            log.info("Certificate list is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(certificateListByPagination, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a Certificate by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/id/{certificateId}")
    public ResponseEntity<CertificateResponse> getCertificateById(@PathVariable("certificateId") Integer certificateId) {
        CertificateResponse serviceCertificateId = this.certificateService.getCertificateId(certificateId);
        return new ResponseEntity<>(serviceCertificateId, HttpStatus.OK);
    }

    @Operation(summary = "Generate  Certificates by Id Using Jasper Report")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    //Generate Report
    @GetMapping("/docs/{format}/{certificateId}")
    public String generateReport(@PathVariable String format, @PathVariable Integer certificateId) throws FileNotFoundException, JRException {
        String reports = jasperReportService.reports(format, certificateId);
        log.info("Document generated of :{}", reports);
        return "document generated";
    }

    @Operation(summary = "Generate  Certificates by Id Using Google Apps Script from Cache database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/documents/cache/{certificateId}")
    public ResponseEntity<String> callGoogleAppsScriptFunctionWithCache(@PathVariable Integer certificateId) {
        String docs = this.documentService.callGoogleAppsScriptFunctionUsingCache(certificateId);
        log.info("Document generated of :{}", docs);
        return new ResponseEntity<>("Document generated", HttpStatus.OK);
    }


    @RateLimited
    @PutMapping("/count/{certificateId}/company/{companyName}")
    public ResponseEntity<CertificateResponse> changeCountGeneratedDocuments(
            @PathVariable("certificateId") int certificateId,
            @PathVariable("companyName") String companyName) {
        CertificateResponse changeCountGeneratingCertificate = this.certificateService.changeCountCompaniesAfterGeneratingCertificate(certificateId, companyName);
        return new ResponseEntity<>(changeCountGeneratingCertificate, HttpStatus.OK);
    }


    @Operation(summary = "Generate Certificates by Id Using Google Apps Script and Properties File Values")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/documents/properties/{certificateId}")
    public String callGoogleAppsScriptFunctionWithProperties(@PathVariable Integer certificateId) {

        String response = this.documentService.callGoogleAppsScriptFunction(certificateId);
        log.info("Document generated of :{}", response);
        return "document generated";
    }


    @Operation(summary = "Candidate size")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/size")
    public ResponseEntity<Integer> getCandidateSize() {
        int certificateSize = this.certificateService.getCertificateSize();
        return new ResponseEntity<>(certificateSize, HttpStatus.OK);
    }

    @Operation(summary = "Check recently added candidates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/topfive")
    public ResponseEntity<List<CertificateResponse>> getTopFiveCertificates() {
        List<CertificateResponse> certificateResponseList = this.certificateService.getTopFiveCertificateList();
        return new ResponseEntity<>(certificateResponseList, HttpStatus.OK);
    }

    @Operation(summary = "Search a Candidate by their name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/name/search/{keywords}")
    public ResponseEntity<List<CertificateResponse>> searchCandidateByName(@PathVariable("keywords") String keywords) {
        List<CertificateResponse> candidateName = this.certificateService.findByCandidateName(keywords);
        return new ResponseEntity<>(candidateName, HttpStatus.OK);
    }

    @Operation(summary = "Change status after generating certificate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @PutMapping("/{certificateId}/status")
    public ResponseEntity<CertificateResponse> changeCompanyStatus(
            @PathVariable("certificateId") int certificateId,
            @RequestParam(value = "status") String status) {
        CertificateResponse changeCertificateStatus = this.certificateService.changeCertificateStatus(certificateId, status);
        return new ResponseEntity<>(changeCertificateStatus, HttpStatus.OK);
    }

    @Operation(summary = "Soft delete a certificate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @PutMapping("/soft/delete/{certificateId}")
    public ResponseEntity<CertificateRequest> sofDeleteCertificate(@PathVariable("certificateId") int certificateId) {
        this.certificateService.isDeletedCertificate(certificateId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Retrieve all certificates list which one pending state")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @GetMapping("/list/change/status/pending/{changeStatus}")
    public ResponseEntity<List<CertificateResponse>> getAllCandidateListChangeStatus(@PathVariable("changeStatus") ChangeStatus changeStatus) {
        List<CertificateResponse> certificateResponseList = this.certificateService.findCandidatesListPendingStatus(changeStatus);
        return new ResponseEntity<>(certificateResponseList, HttpStatus.OK);
    }


    @Operation(summary = "Header size")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RequestMapping(value = "/head/endpoint", method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for certificate endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @Operation(summary = "Options")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RequestMapping(value = "/options/endpoint", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for certificate endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest, HttpStatus.OK);
    }

    @GetMapping("/soft/deleted/list/{deleted}")
    public ResponseEntity<List<CertificateResponse>> getSoftDeletedCertificateList(@PathVariable("deleted") boolean deleted){
        List<CertificateResponse> softDeletedCertificateList = this.certificateService.getSoftDeletedCertifcateList(deleted);
        return new ResponseEntity<>(softDeletedCertificateList,HttpStatus.OK);
    }

    @GetMapping("/soft/false/list/{deleted}")
    public ResponseEntity<List<CertificateResponse>> getSoftDeletedCertificateFalseList(@PathVariable("deleted") boolean deleted){
        List<CertificateResponse> softDeletedCertificateList = this.certificateService.getSoftDeletedFalseListCertificateList(deleted);
        return new ResponseEntity<>(softDeletedCertificateList,HttpStatus.OK);
    }

    @PutMapping("/soft/delete/change/status/{certificateId}")
    public ResponseEntity<CertificateRequest> changeStatusSoftDeleteFalse(@PathVariable("certificateId") int certificateId) {
        this.certificateService.changeStatusSoftDeleteMakeFalse(certificateId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/count/gender")
    public ResponseEntity<List<GenderCountResponse>> getGenderCountList(){
        List<GenderCountResponse> certificateServiceCandidateByGender = this.certificateService.getCandidateByGender();
        return new ResponseEntity<>(certificateServiceCandidateByGender,HttpStatus.OK);
    }

}
