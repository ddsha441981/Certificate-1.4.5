package com.cwc.certificate.services;

import com.cwc.certificate.repository.TaxSlabRepository;
import com.cwc.certificate.service.impl.TaxSlabServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaxSlabServiceImplTests {

    @Autowired
    private TaxSlabServiceImpl taxSlabService;

    @Mock
    private TaxSlabRepository taxSlabRepository;

    @Test
    void setSalaryExposeServiceTest(){
//        while (taxSlabRepository.)
    }
}
