package com.cwc.certificate.controller;

import com.cwc.certificate.dto.request.DefaultSalaryValuesRequest;
import com.cwc.certificate.dto.response.DefaultSalaryValuesResponse;
import com.cwc.certificate.model.DefaultSalaryValues;
import com.cwc.certificate.service.DefaultSalaryValuesService;
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
@RequestMapping("/api/v6/default/salary")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Default Salary", description = "Default Salary management API")
public class DefaultSalaryValuesController {
    private final DefaultSalaryValuesService defaultSalaryValuesService;


    @PostMapping("/")
    public ResponseEntity<DefaultSalaryValues> addDefaultSalaryValues(@RequestBody DefaultSalaryValuesRequest  defaultSalaryValuesRequest){
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesService.addDefaultSalaryValues(defaultSalaryValuesRequest);
        return new ResponseEntity<>(defaultSalaryValues, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultSalaryValues> updateDefaultSalaryValues(@RequestBody DefaultSalaryValuesRequest  defaultSalaryValuesRequest,@PathVariable("id") Integer id){
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesService.updateDefaultSalaryValues(defaultSalaryValuesRequest,id);
        return new ResponseEntity<>(defaultSalaryValues, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DefaultSalaryValues> patchDefaultSalaryValues(@RequestBody DefaultSalaryValuesRequest  defaultSalaryValuesRequest,@PathVariable("id") Integer id){
        DefaultSalaryValues defaultSalaryValues = this.defaultSalaryValuesService.patchDefaultSalaryValues(defaultSalaryValuesRequest,id);
        return new ResponseEntity<>(defaultSalaryValues, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultSalaryValuesResponse> getDefaultSalaryValues(@PathVariable("id") Integer id){
        DefaultSalaryValuesResponse defaultSalaryValues = this.defaultSalaryValuesService.getDefaultSalaryValues(id);
        return new ResponseEntity<>(defaultSalaryValues, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDefaultSalaryValues(@PathVariable("id") Integer id){
         this.defaultSalaryValuesService.deleteDefaultSalaryValues(id);
        return new ResponseEntity<>("delete successfully", HttpStatus.OK);
    }

}
