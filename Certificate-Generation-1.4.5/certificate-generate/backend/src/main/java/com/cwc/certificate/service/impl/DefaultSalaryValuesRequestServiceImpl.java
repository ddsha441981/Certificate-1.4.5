package com.cwc.certificate.service.impl;

import com.cwc.certificate.dto.request.DefaultSalaryValuesRequest;
import com.cwc.certificate.dto.response.DefaultSalaryValuesResponse;
import com.cwc.certificate.exceptions.model.DuplicateDataFound;
import com.cwc.certificate.exceptions.model.ResourceNotFoundException;
import com.cwc.certificate.model.DefaultSalaryValues;
import com.cwc.certificate.repository.DefaultSalaryValuesRepository;
import com.cwc.certificate.service.DefaultSalaryValuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultSalaryValuesRequestServiceImpl implements DefaultSalaryValuesService {

    private final DefaultSalaryValuesRepository  defaultSalaryValuesRepository;
    @Override
    public DefaultSalaryValues addDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest) {
        if (defaultSalaryValuesRequest.getId() == 1){
            throw new DuplicateDataFound("Duplicate key found update your data!!!");
        }
        DefaultSalaryValues defaultSalaryValues = DefaultSalaryValues.builder()
                .hra(defaultSalaryValuesRequest.getHra())
                .gratuity(defaultSalaryValuesRequest.getGratuity())
                .investments80C(defaultSalaryValuesRequest.getInvestments80C())
                .profTax(defaultSalaryValuesRequest.getProfTax())
                .medicalInsurance(defaultSalaryValuesRequest.getMedicalInsurance())
                .build();
        return this.defaultSalaryValuesRepository.save(defaultSalaryValues);
    }

    @Override
    public DefaultSalaryValues updateDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest, int id) {
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found "));
        defaultSalaryValues.setGratuity(defaultSalaryValues.getGratuity());
        defaultSalaryValues.setHra(defaultSalaryValuesRequest.getHra());
        defaultSalaryValues.setProfTax(defaultSalaryValues.getProfTax());
        defaultSalaryValues.setMedicalInsurance(defaultSalaryValues.getMedicalInsurance());
        defaultSalaryValues.setInvestments80C(defaultSalaryValues.getInvestments80C());
        return this.defaultSalaryValuesRepository.save(defaultSalaryValues);
    }

    @Override
    public DefaultSalaryValues patchDefaultSalaryValues(DefaultSalaryValuesRequest defaultSalaryValuesRequest, int id) {
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found "));
        defaultSalaryValues.setGratuity(defaultSalaryValues.getGratuity());
        defaultSalaryValues.setHra(defaultSalaryValuesRequest.getHra());
        defaultSalaryValues.setProfTax(defaultSalaryValues.getProfTax());
        defaultSalaryValues.setMedicalInsurance(defaultSalaryValues.getMedicalInsurance());
        defaultSalaryValues.setInvestments80C(defaultSalaryValues.getInvestments80C());
        return this.defaultSalaryValuesRepository.save(defaultSalaryValues);
    }

    @Override
    public DefaultSalaryValuesResponse getDefaultSalaryValues(int id) {
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found "));
        return mapToDefaultValue(defaultSalaryValues);
    }

    @Override
    public void deleteDefaultSalaryValues(int id) {
        this.defaultSalaryValuesRepository.deleteById(id);
    }

    private DefaultSalaryValuesResponse mapToDefaultValue(DefaultSalaryValues defaultSalaryValues) {
        return DefaultSalaryValuesResponse.builder()
                .id(defaultSalaryValues.getId())
                .hra(defaultSalaryValues.getHra())
                .gratuity(defaultSalaryValues.getGratuity())
                .investments80C(defaultSalaryValues.getInvestments80C())
                .profTax(defaultSalaryValues.getProfTax())
                .medicalInsurance(defaultSalaryValues.getMedicalInsurance())
                .build();
    }
}
