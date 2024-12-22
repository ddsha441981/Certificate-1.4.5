package com.cwc.certificate.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
@Entity
@Table(name = "activity_logs")
public class ActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int activityLogId;
    private String actionType;
    private String entityName;
    private int entityId;
    private String performedBy;
    private LocalDateTime actionTimestamp;
    private String details;
}
