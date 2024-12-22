package com.cwc.certificate.dto.response;

import lombok.*;

import java.time.LocalDateTime;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/02
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ActivityLogResponse {
    private int activityLogId;
    private String actionType;
    private String entityName;
    private int entityId;
    private String performedBy;
    private LocalDateTime actionTimestamp;
    private String details;
}
