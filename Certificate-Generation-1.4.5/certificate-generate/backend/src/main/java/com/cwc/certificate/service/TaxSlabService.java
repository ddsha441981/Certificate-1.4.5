package com.cwc.certificate.service;

import com.cwc.certificate.dto.request.TaxSlabRequest;
import com.cwc.certificate.dto.response.TaxSlabResponse;
import com.cwc.certificate.model.TaxSlab;

import java.util.List;

public interface TaxSlabService {
    TaxSlab addTaxSlab(TaxSlabRequest taxSlabRequest);
    TaxSlab updateTaxSlab(TaxSlabRequest taxSlabRequest,int id);
    TaxSlabResponse getTaxSlab(int id);
    void deleteTaxSlab(int id);
   List<TaxSlabResponse> getTaxSlabList();
}
