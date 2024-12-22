package com.cwc.certificate.controller;

import com.cwc.certificate.dto.response.SalaryExposeResponse;
import com.cwc.certificate.service.SalaryExposeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/06/07
 */


@RestController
@RequestMapping("/api/v6/salary/expose")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Salary Expose", description = "Salary Slab Management API")
public class SalaryExposeController {

    private final SalaryExposeService salaryExposeService;

    @GetMapping("/")
    public ResponseEntity<SalaryExposeResponse> calculateSalaryUsingCTC(@RequestParam(required = true) double ctc,
                                                                        @RequestParam(required = false) Double epf,
                                                                        @RequestParam(required = false) Double performanceBonus){
        SalaryExposeResponse salaryExposeResponse = this.salaryExposeService.calaculateSalaryUsingCTC(ctc, epf, performanceBonus);
        return new ResponseEntity<>(salaryExposeResponse, HttpStatus.OK);
    }
}
