package com.cwc.certificate.controller;

import com.cwc.certificate.dto.request.TaxSlabRequest;
import com.cwc.certificate.dto.response.TaxSlabResponse;
import com.cwc.certificate.model.Certificate;
import com.cwc.certificate.model.TaxSlab;
import com.cwc.certificate.service.TaxSlabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/06/07
 */


@RestController
@RequestMapping("/api/v6/tax/expose")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j

@Tag(name = "Tax Slab", description = "Tax Slab Management API")
public class TaxSlabController {

    private final TaxSlabService taxSlabService;


    @Operation(summary = "Create a new Tax Slab")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })

    @PostMapping("/")
    public ResponseEntity<TaxSlab> addTaxSlab(@RequestBody TaxSlabRequest taxSlabRequest){
        TaxSlab taxSlab = this.taxSlabService.addTaxSlab(taxSlabRequest);
        return new ResponseEntity<>(taxSlab, HttpStatus.OK);
    }

    @Operation(summary = "Update Tax Slab information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })

    @PutMapping("/{id}")
    public ResponseEntity<TaxSlab> updateTaxSlab(@RequestBody TaxSlabRequest taxSlabRequest, @PathVariable("id") int id){
        TaxSlab taxSlab = this.taxSlabService.updateTaxSlab(taxSlabRequest,id);
        return new ResponseEntity<>(taxSlab, HttpStatus.OK);
    }



    @Operation(summary = "Retrieve Tax Slab by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })


    @GetMapping("/{id}")
    public ResponseEntity<TaxSlabResponse> getTaxSlab(@PathVariable("id") int id){
        TaxSlabResponse taxSlab = this.taxSlabService.getTaxSlab(id);
        return new ResponseEntity<>(taxSlab, HttpStatus.OK);
    }

    @Operation(summary = "Delete Tax Slab")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaxSlab(@PathVariable("id") int id){
        this.taxSlabService.deleteTaxSlab(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all Tax Slab list")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Certificate[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })
    @GetMapping("/slabList")
    public ResponseEntity<List<TaxSlabResponse>> taxSlabList(){
        List<TaxSlabResponse> taxSlabList = this.taxSlabService.getTaxSlabList();
        return new ResponseEntity<>(taxSlabList, HttpStatus.OK);
    }
}
