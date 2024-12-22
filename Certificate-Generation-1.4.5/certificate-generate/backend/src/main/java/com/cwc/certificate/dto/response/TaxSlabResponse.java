package com.cwc.certificate.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaxSlabResponse {
    private int id;
    private double slabLimit;
    private double taxRate;
}
