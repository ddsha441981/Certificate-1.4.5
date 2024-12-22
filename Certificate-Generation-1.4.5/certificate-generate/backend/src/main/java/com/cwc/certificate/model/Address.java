package com.cwc.certificate.model;

import com.cwc.certificate.model.enums.AddressType;
import com.cwc.certificate.model.enums.Cities;
import com.cwc.certificate.model.enums.Countries;
import jakarta.persistence.*;
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
@Entity
@ToString
@Table(name = "company_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;
    @Enumerated(EnumType.STRING)
    private Countries country;
    private String zipCode;
    private String buildingNumber;
    @Enumerated(EnumType.STRING)
    private Cities city;
    private String street;
    private String landmark;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}
