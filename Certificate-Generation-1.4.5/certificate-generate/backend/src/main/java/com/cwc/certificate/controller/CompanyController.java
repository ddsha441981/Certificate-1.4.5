package com.cwc.certificate.controller;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.CompanyRequest;
import com.cwc.certificate.dto.request.MessageRequest;
import com.cwc.certificate.dto.response.AddressResponse;
import com.cwc.certificate.dto.response.CompanyPaginationResponse;
import com.cwc.certificate.dto.response.CompanyResponse;
import com.cwc.certificate.dto.response.DocumentsResponse;
import com.cwc.certificate.model.Company;
import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.service.CompanyService;
import com.cwc.certificate.utility.FileValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */


@RestController
@RequestMapping("/api/v1/company")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Company", description = "Company management API")
public class CompanyController {


    @Autowired
    private final FileValidator fileValidator;

    @Autowired
    private final CompanyService companyService;

    @Autowired
    private final ObjectMapper objectMapper;

    @Operation(summary = "Create a new Company")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PostMapping("/")
    public ResponseEntity<String> addCompanyFile(
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("signature") MultipartFile signature,
            @RequestParam("formData") String formData) {
        try {
            if (!fileValidator.isPngFile(logo) || !fileValidator.isPngFile(signature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Only PNG files are allowed for logo and signature.");
            }

            //Form data convert into object
            CompanyRequest companyRequest = objectMapper.readValue(formData, CompanyRequest.class);
            String companyName = companyRequest.getCompanyName();
            log.info("Parsed CompanyRequest: {}", companyRequest);

            log.info("Received formData: {}", formData);
            log.info("Received logo file: {}", logo.getOriginalFilename());
            log.info("Received signature file: {}", signature.getOriginalFilename());

            //Raise Null
//            String logoOriginalFilename = StringUtils.cleanPath(logo.getOriginalFilename());
//            String signatureOriginalFilename = StringUtils.cleanPath(signature.getOriginalFilename());


            Optional<String> originalFilenameLogo = Optional.ofNullable(logo.getOriginalFilename());
            String logoOriginalFilename = originalFilenameLogo.map(StringUtils::cleanPath).orElse(null);

            Optional<String> originalFilenameSignature = Optional.ofNullable(signature.getOriginalFilename());
            String signatureOriginalFilename = originalFilenameSignature.map(StringUtils::cleanPath).orElse(null);


            companyRequest.setCompanyLogo(logoOriginalFilename);
            companyRequest.setSignatureAuthorities(signatureOriginalFilename);

//            if (logoOriginalFilename.contains("..")) {
//                log.error("Invalid file name: {}", logoOriginalFilename);
//                throw new Exception("Invalid file name: " + logoOriginalFilename);
//            }
//            if (signatureOriginalFilename.contains("..")) {
//                log.error("Invalid file name: {}", signatureOriginalFilename);
//                throw new Exception("Invalid file name: " + signatureOriginalFilename);
//            }

            Optional<String> logoFilenameOptional = Optional.ofNullable(logoOriginalFilename);
            if (logoFilenameOptional.isPresent() && logoFilenameOptional.get().contains("..")) {
                log.error("Invalid file name: {}", logoFilenameOptional.get());
                throw new Exception("Invalid file name: " + logoFilenameOptional.get());
            }

            Optional<String> signatureFilenameOptional = Optional.ofNullable(signatureOriginalFilename);
            if (signatureFilenameOptional.isPresent() && signatureFilenameOptional.get().contains("..")) {
                log.error("Invalid file name: {}", signatureFilenameOptional.get());
                throw new Exception("Invalid file name: " + signatureFilenameOptional.get());
            }
            try {
                companyRequest.setLogoData(Base64.getEncoder().encodeToString(logo.getBytes()));
                companyRequest.setSignatureData(Base64.getEncoder().encodeToString(signature.getBytes()));
            } catch (IOException e) {
                log.error("Error processing request:", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process the request. Please try again.");
            }

            log.info("Uploaded files saved successfully. Paths: logo={}, signature={}", logoOriginalFilename, signatureOriginalFilename);

            //company Name save in small case in database
            companyRequest.setCompanyName(companyName.toLowerCase());
            // call database here
            this.companyService.addCompany(companyRequest);

            return new ResponseEntity<>("Company and files saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing request:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the request. Please try again.");
        }
    }


    @Operation(summary = "Update Company information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @PutMapping("/update/{companyId}")
    public ResponseEntity<String> updateCompany(
            @RequestParam("logo") MultipartFile logo,
            @RequestParam("signature") MultipartFile signature,
            @RequestParam("formData") String formData,
            @PathVariable("companyId") Integer companyId){
        try {
            if (!fileValidator.isPngFile(logo) || !fileValidator.isPngFile(signature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Only PNG files are allowed for logo and signature.");
            }

            //Form data convert into object
            CompanyRequest companyRequest = objectMapper.readValue(formData, CompanyRequest.class);
            String companyName = companyRequest.getCompanyName();
            log.info("Parsed CompanyRequest: {}", companyRequest);

            log.info("Received formData: {}", formData);
            log.info("Received logo file: {}", logo.getOriginalFilename());
            log.info("Received signature file: {}", signature.getOriginalFilename());

//            String logoOriginalFilename = StringUtils.cleanPath(logo.getOriginalFilename());
//            String signatureOriginalFilename = StringUtils.cleanPath(signature.getOriginalFilename());

            Optional<String> originalFilenameLogo = Optional.ofNullable(logo.getOriginalFilename());
            String logoOriginalFilename = originalFilenameLogo.map(StringUtils::cleanPath).orElse(null);

            Optional<String> originalFilenameSignature = Optional.ofNullable(signature.getOriginalFilename());
            String signatureOriginalFilename = originalFilenameSignature.map(StringUtils::cleanPath).orElse(null);


            companyRequest.setCompanyLogo(logoOriginalFilename);
            companyRequest.setSignatureAuthorities(signatureOriginalFilename);

//            if (logoOriginalFilename.contains("..")) {
//                log.error("Invalid file name: {}", logoOriginalFilename);
//                throw new Exception("Invalid file name: " + logoOriginalFilename);
//            }
//            if (signatureOriginalFilename.contains("..")) {
//                log.error("Invalid file name: {}", signatureOriginalFilename);
//                throw new Exception("Invalid file name: " + signatureOriginalFilename);
//            }

            Optional<String> logoFilenameOptional = Optional.ofNullable(logoOriginalFilename);
            if (logoFilenameOptional.isPresent() && logoFilenameOptional.get().contains("..")) {
                log.error("Invalid file name: {}", logoFilenameOptional.get());
                throw new Exception("Invalid file name: " + logoFilenameOptional.get());
            }

            Optional<String> signatureFilenameOptional = Optional.ofNullable(signatureOriginalFilename);
            if (signatureFilenameOptional.isPresent() && signatureFilenameOptional.get().contains("..")) {
                log.error("Invalid file name: {}", signatureFilenameOptional.get());
                throw new Exception("Invalid file name: " + signatureFilenameOptional.get());
            }

            try {
                companyRequest.setLogoData(Base64.getEncoder().encodeToString(logo.getBytes()));
                companyRequest.setSignatureData(Base64.getEncoder().encodeToString(signature.getBytes()));
            } catch (IOException e) {
                log.error("Error processing request:", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process the request. Please try again.");
            }

            log.info("Uploaded files saved successfully. Paths: logo={}, signature={}", logoOriginalFilename, signatureOriginalFilename);

            //company Name save in small case in database
            companyRequest.setCompanyName(companyName.toLowerCase());
            // call database here
            this.companyService.updateCompany(companyRequest, companyId);

            return new ResponseEntity<>("Company and files updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error processing request:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process the request. Please try again.");
        }
    }

    @Operation(summary = "Retrieve a company by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getByIdCompany(@PathVariable("companyId") Integer companyId){
        CompanyResponse company = this.companyService.companyById(companyId);
        log.info("Company found {} " ,company.toString());
        return  new ResponseEntity<>(company, HttpStatus.OK);
    }


    @Operation(summary = "Retrieve  documents by company Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/documents/{companyId}")
    public ResponseEntity<DocumentsResponse> getCompanyRelatedDocuments(@PathVariable("companyId") Integer companyId){
        DocumentsResponse documentsResponseList = this.companyService.getDocumentsByCompanyId(companyId);
        log.info("Documents found  {} " ,documentsResponseList.toString());
        return  new ResponseEntity<>(documentsResponseList, HttpStatus.OK);
    }


    @Operation(summary = "Retrieve  address by company Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/address/{companyId}")
    public ResponseEntity<List<AddressResponse>> getAddressList(@PathVariable("companyId") Integer companyId){
        List<AddressResponse> addressResponseList = this.companyService.getCompanyAddressByCompanyId(companyId);
        log.info("Address found {} " ,addressResponseList.toString());
        return new ResponseEntity<>(addressResponseList, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all company list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/list")
    public ResponseEntity<List<CompanyResponse>> getCompanyList(){
        List<CompanyResponse> companyList = this.companyService.getCompanyList();
        log.info("Company list {} " ,companyList);
        return  new ResponseEntity<>(companyList, HttpStatus.OK);
    }


    @Operation(summary = "Retrieve company list using pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @GetMapping("/pagination/list")
    public ResponseEntity<List<CompanyPaginationResponse>> getCompanyListByPagination(
            @RequestParam(value = "pageNumber",defaultValue = ConstantValue.DEFAULT_PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = ConstantValue.DEFAULT_PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = ConstantValue.DEFAULT_SORT_BY_COMPANY,required = false) String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = ConstantValue.DEFAULT_SORT_DIR,required = false) String sortOrder){
        List<CompanyPaginationResponse> companiesListByPagination = this.companyService.getCompaniesListByPagination(pageNumber, pageSize, sortBy, sortOrder);
        if (companiesListByPagination.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity<>(companiesListByPagination, HttpStatus.OK);
    }

    @Operation(summary = "Delete a Company")
    @ApiResponses({
            @ApiResponse(responseCode = "206", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RateLimited
    @DeleteMapping("/{companyId}")
    public MessageRequest deleteComapny(@PathVariable("companyId") Integer companyId){
        this.companyService.deleteComapny(companyId);
        MessageRequest messageRequest =  MessageRequest.builder()
                .message("Company deleted successfully with id: {} " + companyId)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.NO_CONTENT).getBody();
    }

    @Operation(summary = "Check available company size")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/companySize")
    public ResponseEntity<Integer> getCompanySize(){
        Integer companySize = this.companyService.getCompanySize();
        return  new ResponseEntity<>(companySize, HttpStatus.OK);
    }

    @Operation(summary = "Recently added companies list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/topFive")
    public ResponseEntity<List<CompanyResponse>> getCompanyListTopFive(){
        List<CompanyResponse> company = this.companyService.getCompanyListTopFive();
        return  new ResponseEntity<>(company, HttpStatus.OK);
    }


    @Operation(summary = "Search  Company by company name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/search/name/{keywords}")
    public ResponseEntity<List<CompanyResponse>> searchCompanyByName(@PathVariable("keywords") String keywords){
        List<CompanyResponse> searchCompanyByName = this.companyService.searchCompanyByName(keywords);
        return  new ResponseEntity<>(searchCompanyByName, HttpStatus.OK);
    }

    @Operation(summary = "To change Company status Active or Inactive")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RateLimited
    @PutMapping("/companies/{companyId}/status")
    public ResponseEntity<CompanyResponse> changeCompanyStatus(@PathVariable("companyId") int companyId,
                                                               @RequestParam(value = "status") String status){
        CompanyResponse updatedCompanyAfterStatusChange = this.companyService.changeCompanyStatus(companyId, status);
        return  new ResponseEntity<>(updatedCompanyAfterStatusChange, HttpStatus.OK);
    }


    @Operation(summary = "Soft delete a company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @PutMapping("/soft/delete/{companyId}")
    public ResponseEntity<CompanyRequest> softDelete(@PathVariable("companyId") int companyId){
        this.companyService.isDeleted(companyId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Retrieve Active Company for Candidates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @GetMapping("/activecompany")
    public ResponseEntity<List<CompanyResponse>> getActiveCompany(){
        List<CompanyResponse> activeCompany = this.companyService.getCompanyListByActiveStatus();
        return  new ResponseEntity<>(activeCompany, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve InActive Companies list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @GetMapping("/inactivecompany")
    public ResponseEntity<List<CompanyResponse>> getInActiveCompaniesList(){
        List<CompanyResponse> inActiveCompany = this.companyService.getInActiveCompanies();
        return  new ResponseEntity<>(inActiveCompany, HttpStatus.OK);
    }


    @Operation(summary = "Check Header")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @RequestMapping(value = "/head/endpoint",method = RequestMethod.HEAD)
    public ResponseEntity<MessageRequest> getHeadEndpoints(@RequestHeader("Content-Length") String contentLength) {
        log.info("Head request for Company endpoint");
        MessageRequest messageRequest = MessageRequest.builder()
                .message("Content Length :: " + contentLength)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRequest);
    }

    @Operation(summary = "Call any API using OPTIONS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Company.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @RequestMapping(value = "/options/endpoint",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> getOptionsEndpoints() {
        log.info("Options request for Company endpoint");
        String allMethods = "GET, POST, PUT, PATCH, DELETE, OPTIONS";
        MessageRequest messageRequest = MessageRequest.builder()
                .message("All Methods List :: " + allMethods)
                .build();
        return new ResponseEntity<>(messageRequest,HttpStatus.OK);
    }


    @GetMapping("/count/docs")
    public ResponseEntity<?> getCountGeneratedDocumentCompanyWise(){
        List<Pair<String, Integer>> pairs = this.companyService.companyCountDetails();
        System.out.println(pairs);
        return new ResponseEntity<>(pairs, HttpStatus.OK);
    }

    @GetMapping("/soft/deleted/list/{deleted}")
    public ResponseEntity<List<CompanyResponse>> getSoftDeletedCompaniesList(@PathVariable("deleted") boolean deleted){
        List<CompanyResponse> softDeletedCompaniesList = this.companyService.getSoftDeletedCompaniesList(deleted);
        return new ResponseEntity<>(softDeletedCompaniesList,HttpStatus.OK);
    }

    @GetMapping("/soft/deleted/list/false/{deleted}")
    public ResponseEntity<List<CompanyResponse>> getSoftDeletedFalseCompaniesList(@PathVariable("deleted") boolean deleted){
        List<CompanyResponse> softDeletedCompaniesList = this.companyService.getSoftDeletedFalseListCompaniesList(deleted);
        return new ResponseEntity<>(softDeletedCompaniesList,HttpStatus.OK);
    }

    @PutMapping("/soft/delete/change/{companyId}/status/{deleted}")
    public ResponseEntity<CompanyRequest> softDeleteChange(@PathVariable("companyId") int companyId,@PathVariable("deleted") boolean deleted){
        this.companyService.reverseSoftDelete(companyId,deleted);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list/companies/greater/zero")
    public ResponseEntity<List<CompanyResponse>> getCompaniesListGreaterThanZero(){
        List<CompanyResponse> list = this.companyService.findByCompaniesGreaterThanZero();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

//    private boolean isPngFile(MultipartFile file) {
//        return file.getContentType() != null && file.getContentType().equals("image/png");
//    }
}
