package com.cwc.certificate.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MailResponse  {
    private String recipient;
    private boolean status = false;
    private String responseMessage;

}
