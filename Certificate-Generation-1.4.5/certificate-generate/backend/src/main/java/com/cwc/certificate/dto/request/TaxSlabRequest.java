package com.cwc.certificate.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaxSlabRequest {
    private int id;
    private double slabLimit;
    private double taxRate;
}
