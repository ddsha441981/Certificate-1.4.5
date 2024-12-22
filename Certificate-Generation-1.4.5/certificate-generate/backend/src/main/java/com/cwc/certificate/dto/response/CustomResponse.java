package com.cwc.certificate.dto.response;

import com.cwc.certificate.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomResponse<T extends Certificate> {
    private T body;
    private int statusCode;
}
