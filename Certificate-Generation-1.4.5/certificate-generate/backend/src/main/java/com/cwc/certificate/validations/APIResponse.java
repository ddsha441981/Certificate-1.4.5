package com.cwc.certificate.validations;
import lombok.*;
/**
 * Response resource of validate controller.
 *
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/03/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class APIResponse<T> {
    private int code;
    private String message;
    private T responseData;
}
