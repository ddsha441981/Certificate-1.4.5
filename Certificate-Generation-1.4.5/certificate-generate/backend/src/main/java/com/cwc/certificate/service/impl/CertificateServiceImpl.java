package com.cwc.certificate.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.request.CertificateRequest;
import com.cwc.certificate.dto.response.CertificatePaginationResponse;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.dto.response.GenderCountResponse;
import com.cwc.certificate.exceptions.model.CandidateNotFoundException;
import com.cwc.certificate.exceptions.model.CompanyNotFoundException;
import com.cwc.certificate.exceptions.model.EmailAlreadyExistException;
import com.cwc.certificate.exceptions.model.ResourceNotFoundException;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.Company;
import com.cwc.certificate.model.enums.ChangeStatus;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.observer.MessagePublisher;
import com.cwc.certificate.repository.CertificateRepository;
import com.cwc.certificate.repository.CompanyRepository;
import com.cwc.certificate.security.entities.enums.Gender;
import com.cwc.certificate.service.ActivityLogService;
import com.cwc.certificate.service.CertificateService;
import com.cwc.certificate.utility.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Deendayal Kumawat
 * @version 1.4.3
 * @since 2024/02/14
 */

@Service
@Slf4j
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final ActivityLogService activityLogService;

    private final CompanyRepository companyRepository;

    private final MessagePublisher publisher;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, ActivityLogService activityLogService, CompanyRepository companyRepository, MessagePublisher publisher) {
        this.certificateRepository = certificateRepository;
        this.activityLogService = activityLogService;
        this.companyRepository = companyRepository;
        this.publisher = publisher;

        //Subscribe Observer
        publisher.attach(activityLogService);
    }

    @Override
    public Certificate addCertificateData(CertificateRequest certificateRequest) {
        //Process Date
        LocalDate dob = certificateRequest.getDob();
        String formatDate = DateUtils.formatDate(dob);
        LocalDate localDate = DateUtils.parseDate(formatDate);

        //check email if exists then throw exception
        if (this.certificateRepository.existsByCandidateEmail(certificateRequest.getCandidateEmail())) {
            throw new EmailAlreadyExistException("Email already exists with this {} : " + certificateRequest.getCandidateEmail());
        }
        Certificate certificate =  mapToCertificateRequest(certificateRequest,localDate);
        log.info("Certificate request: {}", certificateRequest);
        log.info("Certificate request: {}", certificate.getSalaryInWord());

        //Notify Observer
        publisher.notifyUpdate(new Message("New Candidate : " + certificate.getCandidateName() + " account created."));
        // Log the action in ActivityLog
        log.info("Activity log trigger to capture create new Candidate details...");
        ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                .actionType("New Candidate registered")
                .entityId(certificateRequest.getCertificateId())
                .entityName(certificateRequest.getCompanyName())
                .performedBy(certificateRequest.getCompanyName())
                .actionTimestamp(LocalDateTime.now())
                .details("Adding new Candidate: " + certificateRequest.getCandidateName()
                        + " of company: " + certificateRequest.getCompanyName()
                        + " on date: " + LocalDateTime.now().toString()
                        + ". New candidate added successfully.")
                .build();
        activityLogService.logActivity(activityLogRequest);

        Certificate savedData = this.certificateRepository.save(certificate);
        log.info("Certificate {} is saved", savedData.getCandidateName());

        //Detached Observer
        publisher.detach(activityLogService);
        return savedData;
    }

    @Override
    public boolean checkCandidateEmail(Map<String, String> request) {
        String candidateEmail = request.get("candidateEmail");
        return this.certificateRepository.existsByCandidateEmail(candidateEmail);
    }


    @Override
    public CertificateResponse changeCountCompaniesAfterGeneratingCertificate(int certificateId, String companyName) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new CandidateNotFoundException("Certificate not found with this Id: " + certificateId));

        log.info("Certificate retrieved: {}", certificate);
        String selectedCompany = certificate.getSelectedCompany();
        int companyId = Integer.parseInt(selectedCompany);
        if (certificate.getCountDocs() == 0) {
            log.info("First time document generation for certificateId: {}", certificateId);
            certificate.setCountDocs(1);
            updateCompanyDocumentsCount(companyId, companyName, true);
        } else {
            List<Certificate> certificatesForCompany = certificateRepository.findByCompanyName(companyName);
            boolean isSameCompanyDifferentCertificate = certificatesForCompany.stream()
                    .anyMatch(cert -> cert.getCertificateId() != certificateId);
            if (isSameCompanyDifferentCertificate) {
                certificate.setCountDocs(certificate.getCountDocs() + 1);
                log.info("New certificate generation for company: {}, updating countDocs to {}", companyName, certificate.getCountDocs());
                updateCompanyDocumentsCount(companyId, companyName, false);
            } else {
                log.info("Duplicate document generation for same certificate and company: {}", companyName);
            }
        }
        certificateRepository.save(certificate);
        log.info("Updated certificate saved with countDocs: {}", certificate.getCountDocs());

        return new CertificateResponse(certificate.getCountDocs(), companyName);
    }

    private void updateCompanyDocumentsCount(int companyId, String companyName, boolean isFirstGeneration) {
        Company company = this.companyRepository.findById(companyId).orElseThrow(() -> new CompanyNotFoundException("Comapny not found with this Id"));

        companyName = companyName.toLowerCase();
        if (company.getCompanyName().equals(companyName)) {
            company.setDocumentsCount(company.getDocumentsCount() + 1);
            companyRepository.save(company);
        } else {
            log.info("Company not found in the database: {}", companyName);
        }
    }

    @Override
    public Certificate updateCertificateData(CertificateRequest certificateRequest, int certificateId) {
        Certificate certificate = this.certificateRepository
                .findById(certificateId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with this Id: {} " + certificateId));
        certificate.setCandidateName(certificateRequest.getCandidateName());
        certificate.setCompanyName(certificateRequest.getCompanyName());
        certificate.setCandidateEmail(certificateRequest.getCandidateEmail());
        certificate.setSelectedCompany(certificateRequest.getSelectedCompany());
        certificate.setOfferDate(certificateRequest.getOfferDate());
        certificate.setDateOfJoining(certificateRequest.getDateOfJoining());
        certificate.setDob(certificateRequest.getDob());
        certificate.setCountDocs(certificateRequest.getCountDocs());
        certificate.setGender(certificateRequest.getGender());
        certificate.setJobTitle(certificateRequest.getJobTitle());
        certificate.setEmployeeCode(certificateRequest.getEmployeeCode());
        certificate.setSalaryExpose(certificateRequest.getSalaryExpose());
        certificate.setSelectedDocumentTypes(certificateRequest.getSelectedDocumentTypes());
        certificate.setBank(certificateRequest.getBank());
        certificate.setIdentificationDetails(certificateRequest.getIdentificationDetails());
//        certificate.setChangeStatus(certificateRequest.getChangeStatus());
        certificate.setStatus(ChangeStatus.UPDATED);
        certificate.setChangeStatus(ChangeStatus.PENDING);
        Certificate updatedData = this.certificateRepository.save(certificate);
        log.info("Certificate {} is saved", updatedData.getCandidateName());
        return updatedData;
    }

    @Override
    public List<CertificateResponse> allCertificates() {
        List<Certificate> certificateList = this.certificateRepository.findAll();
        log.info("Certificate list {} ", certificateList);
        return certificateList.stream().map(this::mapToCertificateResponse).toList();
    }

    @Override
    public CertificateResponse getCertificateId(int certificateId) {
        Certificate certificate = this.certificateRepository
                .findById(certificateId).orElseThrow(() -> new CandidateNotFoundException("Candidate not found with this Id: {} " + certificateId));
        log.info("Certificate Id: " + certificateId);
        return mapToCertificateResponse(certificate);
    }

    @Override
    public void deleteCertificate(int certificateId) {
        this.certificateRepository.deleteById(certificateId);
        log.info("Certificate deleted {} ", certificateId);
    }

    @Override
    public int getCertificateSize() {
        return this.certificateRepository.findAll().size();
    }

    @Override
    public List<CertificateResponse> getTopFiveCertificateList() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Certificate> certificateList = this.certificateRepository.findAll(pageable).getContent();
        log.info("Certificate list {} ", certificateList);
        return certificateList.stream().map(this::mapToCertificateResponse).toList();
    }

    @Override
    public List<CertificatePaginationResponse> getCertificateListByPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase(ConstantValue.DEFAULT_SORT_DIR)) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Certificate> certificateList = this.certificateRepository.findAll(pages);
        List<Certificate> certificateListContent = certificateList.getContent();
        List<CertificateResponse> certificateResponseList = certificateListContent.stream().map((this::mapToCertificateResponse)).toList();
        CertificatePaginationResponse certificatePaginationResponse = CertificatePaginationResponse.builder()
                .certificateResponseList(certificateResponseList)
                .pageNumber(certificateList.getTotalPages())
                .pageSize(certificateList.getSize())
                .totalElements(certificateList.getTotalElements())
                .totalPages(certificateList.getTotalPages())
                .lastPage(certificateList.isLast())
                .build();
        log.info("Certificate list using pagination {} ", certificateListContent);
        return Collections.singletonList(certificatePaginationResponse);
    }

    @Override
    public List<CertificateResponse> findByCandidateName(String keywords) {
        List<Certificate> byCandidateName = this.certificateRepository.findByCandidateName("%" + keywords + "%");
        if (byCandidateName.isEmpty()) {
            throw new CandidateNotFoundException("Candidate not found with this name: {} " + keywords);
        }
        return byCandidateName.stream().map(this::mapToCertificateResponse).toList();
    }

    @Override
    public CertificateResponse changeCertificateStatus(int certificateId, String changeStatus) {
        Certificate certificate = this.certificateRepository.findById(certificateId).orElseThrow(() -> new CandidateNotFoundException("Candidate not found with this Id: {} " + certificateId));
        if (changeStatus.equalsIgnoreCase(ConstantValue.GENERATED_ACTIVE_STATUS)) {
            certificate.setChangeStatus(ChangeStatus.GENERATED);
        } else if (changeStatus.equalsIgnoreCase(ConstantValue.UPDATE_PENDING_INACTIVE_STATUS)) {
            certificate.setChangeStatus(ChangeStatus.GENERATED);
        } else {
            throw new IllegalArgumentException("Invalid status: " + changeStatus);
        }
        this.certificateRepository.save(certificate);
        log.info("Certificate status changed: {} to {}", certificate.getCandidateName(), certificate.getChangeStatus());
        return mapToCertificateResponse(certificate);
    }

    @Override
    public void isDeletedCertificate(int certificateId) {
        Optional<Certificate> certificate = this.certificateRepository.findById(certificateId);
        certificate.ifPresent(certificateFound -> {
            certificateFound.setDeleted(true);
            this.certificateRepository.save(certificateFound);
        });
    }

    @Transactional
    @Override
    public List<CertificateResponse> findCandidatesListPendingStatus(ChangeStatus changeStatus) {
        List<Certificate> generatedStatus = this.certificateRepository.findByChangeStatus(changeStatus);
        return generatedStatus.stream().map(this::mapToCertificateResponse).toList();
    }

    @Transactional
    @Override
    public List<CertificateResponse> getSoftDeletedCertifcateList(boolean deleted) {
        List<Certificate> certificateDeletedTrueList = this.certificateRepository.findByDeleted(deleted);
        return certificateDeletedTrueList.stream().map((this::mapToCertificateResponse)).toList();
    }

    @Transactional
    @Override
    public List<CertificateResponse> getSoftDeletedFalseListCertificateList(boolean deleted) {
        List<Certificate> certificateDeletedFalseList = this.certificateRepository.findByDeleted(deleted);
        return certificateDeletedFalseList.stream().map((this::mapToCertificateResponse)).toList();
    }

    @Override
    public void changeStatusSoftDeleteMakeFalse(int certificateId) {
        Certificate certificate = this.certificateRepository.findById(certificateId).orElseThrow(() -> new CandidateNotFoundException("Candidate not found "));
        certificate.setDeleted(false);
        certificateRepository.save(certificate);
    }

    @Override
    public List<GenderCountResponse> getCandidateByGender() {
        List<Certificate> certificateList = this.certificateRepository.findAll();
        Map<Gender, Long> genderCount = certificateList.stream()
                .collect(Collectors.groupingBy(Certificate::getGender, Collectors.counting()));
        List<GenderCountResponse> genderCountResponses = genderCount.entrySet().stream()
                .map(entry -> mapToCertificateGenderResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        log.info("Candidate filtered count by gender : {} " , genderCountResponses);
        return genderCountResponses;
    }

    private GenderCountResponse mapToCertificateGenderResponse(Gender gender, Long count) {
        return GenderCountResponse.builder()
                .gender(gender)
                .count(count)
                .build();
    }

    private Certificate mapToCertificateRequest(CertificateRequest certificateRequest,LocalDate localDate) {
        return Certificate.builder()
                .certificateId(certificateRequest.getCertificateId())
                .candidateName(certificateRequest.getCandidateName())
                .candidateEmail(certificateRequest.getCandidateEmail())
                .companyName(certificateRequest.getCompanyName())
                .selectedCompany(certificateRequest.getSelectedCompany())
                .offerDate(certificateRequest.getOfferDate())
                .dateOfJoining(certificateRequest.getDateOfJoining())
                .dob(localDate)
                .countDocs(certificateRequest.getCountDocs())
                .gender(certificateRequest.getGender())
                .jobTitle(certificateRequest.getJobTitle())
                .employeeCode(certificateRequest.getEmployeeCode())
                .salaryExpose(certificateRequest.getSalaryExpose())
                .selectedDocumentTypes(certificateRequest.getSelectedDocumentTypes())
                .changeStatus(ChangeStatus.PENDING)
                .status(ChangeStatus.CREATED)
                .deleted(false)
                .salaryInWord(certificateRequest.getSalaryInWord())
                .bank(certificateRequest.getBank())
                .identificationDetails(certificateRequest.getIdentificationDetails())
                .build();
    }

    private CertificateResponse mapToCertificateResponse(Certificate certificate) {
        log.info("Certificate mapped {} ", certificate.getCandidateName());
        return CertificateResponse.builder()
                .certificateId(certificate.getCertificateId())
                .candidateName(certificate.getCandidateName())
                .companyName(certificate.getCompanyName())
                .candidateEmail(certificate.getCandidateEmail())
                .selectedCompany(certificate.getSelectedCompany())
                .offerDate(certificate.getOfferDate())
                .dateOfJoining(certificate.getDateOfJoining())
                .dob(certificate.getDob())
                .countDocs(certificate.getCountDocs())
                .gender(certificate.getGender())
                .jobTitle(certificate.getJobTitle())
                .employeeCode(certificate.getEmployeeCode())
                .salaryExpose(certificate.getSalaryExpose())
                .selectedDocumentTypes(certificate.getSelectedDocumentTypes())
                .changeStatus(certificate.getChangeStatus())
                .salaryInWord(certificate.getSalaryInWord())
                .status(certificate.getStatus())
                .deleted(certificate.isDeleted())
                .bank(certificate.getBank())
                .identificationDetails(certificate.getIdentificationDetails())
                .build();
    }
}
