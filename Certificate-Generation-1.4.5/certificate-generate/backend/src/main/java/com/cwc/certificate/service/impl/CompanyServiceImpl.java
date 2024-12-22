package com.cwc.certificate.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.request.CompanyRequest;
import com.cwc.certificate.dto.response.AddressResponse;
import com.cwc.certificate.dto.response.CompanyPaginationResponse;
import com.cwc.certificate.dto.response.CompanyResponse;
import com.cwc.certificate.dto.response.DocumentsResponse;
import com.cwc.certificate.exceptions.model.CompanyNotFoundException;
import com.cwc.certificate.exceptions.model.MyFileNotFoundException;
import com.cwc.certificate.exceptions.model.ResourceNotFoundException;
import com.cwc.certificate.model.Address;
import com.cwc.certificate.model.Company;
import com.cwc.certificate.model.Documents;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.repository.AddressRepository;
import com.cwc.certificate.repository.CompanyRepository;
import com.cwc.certificate.repository.DocumentsLinkRepository;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final DocumentsLinkRepository documentsLinkRepository;
    private final AddressRepository addressRepository;
    private final ActivityLogService activityLogService;
    private final MessagePublisher publisher;



    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, DocumentsLinkRepository documentsLinkRepository, AddressRepository addressRepository, ActivityLogService activityLogService,MessagePublisher publisher) {
        this.companyRepository = companyRepository;
        this.documentsLinkRepository = documentsLinkRepository;
        this.addressRepository = addressRepository;
        this.activityLogService = activityLogService;
        this.publisher = publisher;

        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    @Override
    public Company addCompany(CompanyRequest comapnyRequest) {
        try{
            Company company = mapToCompanyRequest(comapnyRequest);
            log.info("Company saved {} " ,company.getCompanyName());
            //Notify Observer
            publisher.notifyUpdate(new Message("New Company : " + company.getCompanyName() + " created."));

            // Log the action in ActivityLog
            log.info("Activity log trigger to capture create new Company details...");
            ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                    .actionType("New Company Created")
                    .entityId(comapnyRequest.getCompanyId())
                    .entityName(comapnyRequest.getCompanyName())
                    .performedBy(comapnyRequest.getCompanyName())
                    .actionTimestamp(LocalDateTime.now())
                    .details("Adding new Company: " + comapnyRequest.getCompanyName()
                            + " of company: " + comapnyRequest.getCompanyName()
                            + " on date: " + LocalDateTime.now().toString()
                            + ". New Company added successfully.")
                    .build();
            activityLogService.logActivity(activityLogRequest);

            //Detached Observer
            publisher.detach(activityLogService);

            Company savedCompany = this.companyRepository.save(company);
            return savedCompany;
        }catch (Exception e){
            log.info("File not found: {}", comapnyRequest.getCompanyLogo());
            throw new MyFileNotFoundException("File not found: " + comapnyRequest.getCompanyLogo(), e);
        }

    }


    @Override
    public Company updateCompany(CompanyRequest comapnyRequest, int companyId) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Company name not found with Id: {} " + companyId));
        company.setCompanyName(comapnyRequest.getCompanyName());
        company.setChangeStatus(ChangeStatus.ACTIVE);
        company.setStatus(ChangeStatus.UPDATED);
        company.setDeleted(false);
        company.setDocuments(comapnyRequest.getDocuments());
        company.setAddresses(comapnyRequest.getAddresses());
        company.setManagerDetails(comapnyRequest.getManagerDetails());
        company.setHrDetails(comapnyRequest.getHrDetails());
        company.setCompanyEmail(comapnyRequest.getCompanyEmail());
        company.setCompanyLogo(comapnyRequest.getCompanyLogo());
        company.setCompanyPhone(comapnyRequest.getCompanyPhone());
        company.setCompanyWebsite(comapnyRequest.getCompanyWebsite());
        company.setSignatureAuthorities(comapnyRequest.getSignatureAuthorities());
        company.setImageLogoType(ConstantValue.DEFAULT_IMAGE_FORMAT);
        company.setImageSignatureType(ConstantValue.DEFAULT_IMAGE_FORMAT);
        company.setCompanyRegistrationNumber(comapnyRequest.getCompanyRegistrationNumber());
        company.setCompanyDomainType(comapnyRequest.getCompanyDomainType());
        company.setIndustryType(comapnyRequest.getIndustryType());
        company.setYearOfEstablishment(comapnyRequest.getYearOfEstablishment());
        company.setCompanySize(comapnyRequest.getCompanySize());
        company.setCompanyFounder(comapnyRequest.getCompanyFounder());
        company.setCompanyRevenue(comapnyRequest.getCompanyRevenue());
        company.setCompanyLicenseNumber(comapnyRequest.getCompanyLicenseNumber());
        log.info("Company updated {} " ,company.getCompanyName());
        return this.companyRepository.save(company);
    }
    @Override
    public CompanyResponse companyById(int companyId) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company name not found with Id: {} " + companyId));
        log.info("Company found {} " ,company.getCompanyName());
        return mapToCompanyResponse(company);
    }
    @Override
    public List<CompanyResponse> getCompanyList() {
        List<Company> companyList = this.companyRepository.findAll();
        log.info("Company list {} " ,companyList);
        return companyList.stream().map((this::mapToCompanyResponse)).toList();
    }

    @Override
    public void deleteComapny(int companyId) {
        this.companyRepository.deleteById(companyId);
        log.info("Company deleted {} " ,companyId);
    }

    @Override
    public DocumentsResponse getDocumentsByCompanyId(int companyId) {
        Documents documents = this.documentsLinkRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Documents not found with Id: {} " + companyId));
        log.info("Documents found {} " ,documents);
        return mapToDocumentsResponse(documents);
    }

    @Override
    public List<AddressResponse> getCompanyAddressByCompanyId(int companyId) {
        Address address = this.addressRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Address not found with Id: {} " + companyId));
        log.info("Address found {} " ,address);
        return mapToAddressResponse(address);
    }



    @Override
    public int getCompanySize() {
        return this.companyRepository.findAll().size();
    }

    @Override
    public List<CompanyResponse> getCompanyListTopFive() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Company> companyList = this.companyRepository.findAll(pageable).getContent();
        log.info("Company list {} " ,companyList);
        return companyList.stream().map((this::mapToCompanyResponse)).toList();
    }

    //get companies list by pagination
    @Override
    public List<CompanyPaginationResponse> getCompaniesListByPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase(ConstantValue.DEFAULT_SORT_DIR)) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pages = PageRequest.of(pageNumber, pageSize,sort);
        Page<Company> companyList = this.companyRepository.findAll(pages);
        List<Company> companyListContent = companyList.getContent();
        List<CompanyResponse> companyResponseList = companyListContent.stream().map((this::mapToCompanyResponse)).toList();
        CompanyPaginationResponse paginationResponse = CompanyPaginationResponse.builder()
                .companyResponseList(companyResponseList)
                .pageNumber(companyList.getTotalPages())
                .pageSize(companyList.getSize())
                .totalElements(companyList.getTotalElements())
                .totalPages(companyList.getTotalPages())
                .lastPage(companyList.isLast())
                .build();
        log.info("Company list using pagination {} " ,companyListContent);
        return Collections.singletonList(paginationResponse);
    }

    @Override
    public List<CompanyResponse> searchCompanyByName(String keywords) {
        List<Company> companyName = this.companyRepository.findByCompanyName(keywords);
        if (companyName.isEmpty()){
            throw new CompanyNotFoundException("Company not found with this name: {} " + keywords);
        }
        return companyName.stream().map((this::mapToCompanyResponse)).toList();
    }

    @Override
    public CompanyResponse changeCompanyStatus(int companyId, String status) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found with Id: {} " + companyId));
        if (status.equalsIgnoreCase(ConstantValue.UPDATE_ACTIVE_STATUS)){
            company.setChangeStatus(ChangeStatus.ACTIVE);
        }else if (status.equalsIgnoreCase(ConstantValue.UPDATE_INACTIVE_STATUS)){
            company.setChangeStatus(ChangeStatus.INACTIVE);
        }else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.companyRepository.save(company);
        log.info("Company status changed: {} to {}", company.getCompanyName(), company.getChangeStatus());
        return mapToCompanyResponse(company);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<CompanyResponse> getCompanyListByActiveStatus() {
        List<Company> companyList = this.companyRepository.findByChangeStatus(ChangeStatus.ACTIVE);
        if (companyList.isEmpty()){
            throw new CompanyNotFoundException("Company not found with this name: {} " + companyList);
        }
        log.info("Company list of active {} " ,companyList);
        return companyList.stream().map((this::mapToCompanyResponse)).toList();
    }

    @Override
    public List<CompanyResponse> getInActiveCompanies() {
        List<Company> inActiveCompanyList = this.companyRepository.findByChangeStatus(ChangeStatus.INACTIVE);
        if (inActiveCompanyList.isEmpty()){
            throw new CompanyNotFoundException("Company not found with this name: {} " + inActiveCompanyList);
        }
        log.info("Company list of inactive {} " ,inActiveCompanyList);
        return inActiveCompanyList.stream().map((this::mapToCompanyResponse)).toList();
    }


    @Override
    public void isDeleted(int companyId) {
        Optional<Company> companyOpt = this.companyRepository.findById(companyId);
        companyOpt.ifPresentOrElse(company -> {
            company.setDeleted(true);
            if (company.isDeleted()) {
                company.setChangeStatus(ChangeStatus.INACTIVE);
            } else {
                company.setChangeStatus(ChangeStatus.ACTIVE);
            }
//            ChangeStatus status = company.getChangeStatus();
            this.companyRepository.save(company);
        }, () -> {
            throw new IllegalArgumentException("Company with ID " + companyId + " not found");
        });
    }

    @Override
    public List<Pair<String, Integer>> companyCountDetails() {
        List<Company> companyList = this.companyRepository.findAll();
        List<Pair<String, Integer>> companyPairs = companyList.stream()
                .map(company -> Pair.of(company.getCompanyName(), company.getDocumentsCount()))
                .collect(Collectors.toList());
        return companyPairs;
    }

    @Transactional
    @Override
    public List<CompanyResponse> getSoftDeletedCompaniesList(boolean deleted) {
        List<Company> byIsDeletedFalseCompaniesList = this.companyRepository.findByDeleted(deleted);
        return byIsDeletedFalseCompaniesList.stream().map((this::mapToCompanyResponse)).toList();
    }

    @Transactional
    @Override
    public List<CompanyResponse> getSoftDeletedFalseListCompaniesList(boolean deleted) {
        List<Company> byIsDeletedFalseCompaniesList = this.companyRepository.findByDeleted(deleted);
        return byIsDeletedFalseCompaniesList.stream().map((this::mapToCompanyResponse)).toList();
    }

    /**
     * @param companyId
     * @param deleted
     */
    @Override
    public void reverseSoftDelete(int companyId, boolean deleted) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        company.setDeleted(false);
        company.setChangeStatus(ChangeStatus.ACTIVE);
        this.companyRepository.save(company);
    }


    @Override
    public List<CompanyResponse> findByCompaniesGreaterThanZero() {
        List<Company> companyList = this.companyRepository.findByDocumentsCountGreaterThan(0);
        return companyList.stream().map((this::mapToCompanyResponse)).collect(Collectors.toList());
    }

    private List<AddressResponse> mapToAddressResponse(Address address) {
        return List.of(
                AddressResponse.builder()
                        .addressId(address.getAddressId())
                        .country(address.getCountry())
                        .zipCode(address.getZipCode())
                        .buildingNumber(address.getBuildingNumber())
                        .city(address.getCity())
                        .street(address.getStreet())
                        .landmark(address.getLandmark())
                        .addressType(address.getAddressType())
                        .build()
        );
    }

    private DocumentsResponse mapToDocumentsResponse(Documents documents) {
        return DocumentsResponse.builder()
                .apparisalLetterUrl(documents.getApparisalLetterUrl())
                .experienceLetterUrl(documents.getExperienceLetterUrl())
                .offerLetterUrl(documents.getOfferLetterUrl())
                .salarySlipUrl(documents.getSalarySlipUrl())
                .incrementLetterUrl(documents.getIncrementLetterUrl())
                .relievingLetterUrl(documents.getRelievingLetterUrl())
                .build();
    }


    private CompanyResponse mapToCompanyResponse(Company company) {
        log.info("Company mapped {} " ,company.getCompanyName());
        return CompanyResponse.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .changeStatus(company.getChangeStatus())
                .companyEmail(company.getCompanyEmail())
                .companyPhone(company.getCompanyPhone())
                .addresses(company.getAddresses())
                .hrDetails(company.getHrDetails())
                .managerDetails(company.getManagerDetails())
                .companyWebsite(company.getCompanyWebsite())
                .companyLogo(company.getCompanyLogo())
                .status(company.getStatus())
                .deleted(company.isDeleted())
                .logoData(company.getLogoData())
                .signatureData(company.getSignatureData())
                .imageLogoType(company.getImageLogoType())
                .imageSignatureType(company.getImageSignatureType())
                .signatureAuthorities(company.getSignatureAuthorities())
                .documents(company.getDocuments())
                .companyRegistrationNumber(company.getCompanyRegistrationNumber())
                .companyDomainType(company.getCompanyDomainType())
                .industryType(company.getIndustryType())
                .yearOfEstablishment(company.getYearOfEstablishment())
                .companySize(company.getCompanySize())
                .companyFounder(company.getCompanyFounder())
                .companyRevenue(company.getCompanyRevenue())
                .companyLicenseNumber(company.getCompanyLicenseNumber())
                .build();
    }

    private Company mapToCompanyRequest(CompanyRequest comapnyRequest) {
        return Company.builder()
                .companyName(comapnyRequest.getCompanyName())
                .documents(comapnyRequest.getDocuments())
                .changeStatus(ChangeStatus.ACTIVE)
                .deleted(false)
                .status(ChangeStatus.CREATED)
                .addresses(comapnyRequest.getAddresses())
                .companyEmail(comapnyRequest.getCompanyEmail())
                .companyLogo(comapnyRequest.getCompanyLogo())
                .companyPhone(comapnyRequest.getCompanyPhone())
                .companyWebsite(comapnyRequest.getCompanyWebsite())
                .signatureAuthorities(comapnyRequest.getSignatureAuthorities())
                .imageLogoType(ConstantValue.DEFAULT_IMAGE_FORMAT)
                .imageSignatureType(ConstantValue.DEFAULT_IMAGE_FORMAT)
                .logoData(comapnyRequest.getLogoData())
                .signatureData(comapnyRequest.getSignatureData())
                .companyRegistrationNumber(comapnyRequest.getCompanyRegistrationNumber())
                .companyDomainType(comapnyRequest.getCompanyDomainType())
                .industryType(comapnyRequest.getIndustryType())
                .yearOfEstablishment(comapnyRequest.getYearOfEstablishment())
                .companySize(comapnyRequest.getCompanySize())
                .companyFounder(comapnyRequest.getCompanyFounder())
                .companyRevenue(comapnyRequest.getCompanyRevenue())
                .companyLicenseNumber(comapnyRequest.getCompanyLicenseNumber())
                .managerDetails(comapnyRequest.getManagerDetails())
                .hrDetails(comapnyRequest.getHrDetails())
                .build();
    }
}
