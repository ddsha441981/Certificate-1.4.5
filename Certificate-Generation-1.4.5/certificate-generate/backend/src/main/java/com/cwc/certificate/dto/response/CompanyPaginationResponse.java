package com.cwc.certificate.dto.response;

import lombok.*;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompanyPaginationResponse {

    private List<CompanyResponse> companyResponseList;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;

}
