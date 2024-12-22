package com.cwc.certificate.model;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "default_salary_values")
public class DefaultSalaryValues implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    private double gratuity;
    private double hra;
    private double profTax;
    private double medicalInsurance;
    private double investments80C; //= 0.0;

    /**
     @TODO : Make Id is persist
     */
    @PrePersist
    public void ensureIdIsSet(){
        if (this.id == 0){
            this.id = 1;
        }
    }

}

