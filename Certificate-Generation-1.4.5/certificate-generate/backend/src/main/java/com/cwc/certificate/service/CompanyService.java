package com.cwc.certificate.service;

import com.cwc.certificate.model.Company;
import com.cwc.certificate.dto.request.CompanyRequest;
import com.cwc.certificate.dto.response.AddressResponse;
import com.cwc.certificate.dto.response.CompanyPaginationResponse;
import com.cwc.certificate.dto.response.CompanyResponse;
import com.cwc.certificate.dto.response.DocumentsResponse;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */

public interface CompanyService {

    Company addCompany(CompanyRequest comapnyRequest);
    Company updateCompany(CompanyRequest comapnyRequest, int companyId);
    CompanyResponse companyById(int companyId);
    List<CompanyResponse> getCompanyList();
    void deleteComapny(int companyId);
    //get documents for company using company Id
    DocumentsResponse getDocumentsByCompanyId(int companyId);
    List<AddressResponse> getCompanyAddressByCompanyId(int companyId);
    int getCompanySize();
    List<CompanyResponse> getCompanyListTopFive();
    List<CompanyPaginationResponse> getCompaniesListByPagination(Integer pageNumber, Integer pageSize, String sortByCompany, String sortDir);
    List<CompanyResponse> searchCompanyByName(String keywords);
    CompanyResponse changeCompanyStatus(int companyId, String status);
    List<CompanyResponse> getCompanyListByActiveStatus();
    List<CompanyResponse> getInActiveCompanies();
    void isDeleted(int companyId);
    List<Pair<String, Integer>> companyCountDetails();
    List<CompanyResponse> getSoftDeletedCompaniesList(boolean deleted);
    List<CompanyResponse> getSoftDeletedFalseListCompaniesList(boolean deleted);
    void reverseSoftDelete(int companyId, boolean deleted);
    List<CompanyResponse> findByCompaniesGreaterThanZero();
}

