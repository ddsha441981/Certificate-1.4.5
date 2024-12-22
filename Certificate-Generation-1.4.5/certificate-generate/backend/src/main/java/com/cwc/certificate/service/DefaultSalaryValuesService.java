package com.cwc.certificate.service;

import com.cwc.certificate.dto.request.DefaultSalaryValuesRequest;
import com.cwc.certificate.dto.response.DefaultSalaryValuesResponse;
import com.cwc.certificate.model.DefaultSalaryValues;

public interface DefaultSalaryValuesService {
    DefaultSalaryValues addDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest);
    DefaultSalaryValues updateDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest,int id);
    DefaultSalaryValues patchDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest,int id);
    DefaultSalaryValuesResponse getDefaultSalaryValues(int id);
    void deleteDefaultSalaryValues(int id);
}
