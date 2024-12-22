package com.cwc.certificate.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class EmailDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String recipient;
    private String msgBody;
    private String subject;
}
