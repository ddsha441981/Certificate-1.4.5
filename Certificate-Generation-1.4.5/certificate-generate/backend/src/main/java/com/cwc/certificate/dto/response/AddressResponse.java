package com.cwc.certificate.dto.response;

import com.cwc.certificate.model.enums.AddressType;
import com.cwc.certificate.model.enums.Cities;
import com.cwc.certificate.model.enums.Countries;
import lombok.*;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class AddressResponse {

    private int addressId;
    private Countries country;
    private String zipCode;
    private String buildingNumber;
    private Cities city;
    private String street;
    private String landmark;
    private AddressType addressType;
}