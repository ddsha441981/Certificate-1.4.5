package com.cwc.certificate.service;

import com.cwc.certificate.dto.request.CertificateRequest;
import com.cwc.certificate.dto.response.CertificatePaginationResponse;
import com.cwc.certificate.dto.response.CertificateResponse;
import com.cwc.certificate.dto.response.GenderCountResponse;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.enums.ChangeStatus;
import java.util.List;
import java.util.Map;
/**
 * @author  Deendayal Kumawat
 * @version 1.4.3
 * @since   2024/02/14
 */

public interface CertificateService {
    Certificate addCertificateData(CertificateRequest certificateRequest);
    CertificateResponse changeCountCompaniesAfterGeneratingCertificate(int certificateId, String companyName);
    Certificate updateCertificateData(CertificateRequest certificateRequest, int certificateId);
    List<CertificateResponse> allCertificates();
    CertificateResponse getCertificateId(int certificateId);
    void deleteCertificate(int certificateId);
    int getCertificateSize();
    List<CertificateResponse> getTopFiveCertificateList();
    List<CertificatePaginationResponse> getCertificateListByPagination(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    List<CertificateResponse> findByCandidateName(String keywords);
    CertificateResponse changeCertificateStatus(int certificateId, String status);
    void isDeletedCertificate(int certificateId);
    boolean checkCandidateEmail(Map<String , String> request);
    List<CertificateResponse> findCandidatesListPendingStatus(ChangeStatus changeStatus);
    List<CertificateResponse> getSoftDeletedCertifcateList(boolean deleted);
    List<CertificateResponse> getSoftDeletedFalseListCertificateList(boolean deleted);
    void changeStatusSoftDeleteMakeFalse(int certificateId);
    List<GenderCountResponse> getCandidateByGender();
}
