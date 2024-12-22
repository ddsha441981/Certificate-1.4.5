package com.cwc.certificate.service.impl;

import com.cwc.certificate.dto.request.TaxSlabRequest;
import com.cwc.certificate.dto.response.TaxSlabResponse;
import com.cwc.certificate.exceptions.model.ResourceNotFoundException;
import com.cwc.certificate.model.TaxSlab;
import com.cwc.certificate.repository.TaxSlabRepository;
import com.cwc.certificate.service.TaxSlabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxSlabServiceImpl  implements TaxSlabService {

    private final TaxSlabRepository taxSlabRepository;

    @Override
    public TaxSlab addTaxSlab(TaxSlabRequest taxSlabRequest) {
        TaxSlab taxSlab = TaxSlab.builder()
                .taxRate(taxSlabRequest.getTaxRate())
                .slabLimit(taxSlabRequest.getSlabLimit())
                .build();
        return this.taxSlabRepository.save(taxSlab);
    }

    @Override
    public TaxSlab updateTaxSlab(TaxSlabRequest taxSlabRequest, int id) {
        TaxSlab taxSlab = this.taxSlabRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tax not found"));
        taxSlab.setSlabLimit(taxSlabRequest.getSlabLimit());
        taxSlab.setTaxRate(taxSlab.getTaxRate());
        return this.taxSlabRepository.save(taxSlab);
    }

    @Override
    public TaxSlabResponse getTaxSlab(int id) {
        TaxSlab taxSlab = this.taxSlabRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tax not found"));
        return mapToTaxSlab(taxSlab);
    }

    @Override
    public void deleteTaxSlab(int id) {
        this.taxSlabRepository.deleteById(id);
    }

    @Override
    public List<TaxSlabResponse> getTaxSlabList() {
        List<TaxSlab> slabList = this.taxSlabRepository.findAll();
        return slabList.stream().map((slab)->mapToTaxSlab(slab)).collect(Collectors.toList());
    }

    private TaxSlabResponse mapToTaxSlab(TaxSlab taxSlab) {
        return TaxSlabResponse.builder()
                .id(taxSlab.getId())
                .taxRate(taxSlab.getTaxRate())
                .slabLimit(taxSlab.getSlabLimit())
                .build();
    }
}
