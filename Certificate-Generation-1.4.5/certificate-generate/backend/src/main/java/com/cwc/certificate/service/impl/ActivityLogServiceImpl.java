package com.cwc.certificate.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.response.ActivityLogResponse;
import com.cwc.certificate.model.ActivityLog;
import com.cwc.certificate.observer.Message;
import com.cwc.certificate.repository.ActivityLogRepository;
import com.cwc.certificate.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/02
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public void logActivity(ActivityLogRequest activityLogRequest) {
        ActivityLog activityLog = ActivityLog.builder()
                .actionType(activityLogRequest.getActionType())
                .entityId(activityLogRequest.getEntityId())
                .entityName(activityLogRequest.getEntityName())
                .performedBy(activityLogRequest.getPerformedBy())
                .actionTimestamp(activityLogRequest.getActionTimestamp())
                .details(activityLogRequest.getDetails())
                .build();
        activityLogRepository.save(activityLog);
    }
    @Override
    public List<ActivityLogResponse> getLogActivityDetails() {
        Pageable pageable = PageRequest.of(ConstantValue.DEFAULT_PAGE_NUMBER_ACTIVITY_LOG, ConstantValue.DEFAULT_PAGE_SIZE_ACTIVITY_LOG, Sort.by(ConstantValue.DEFAULT_SORT_BY_ACTIVITY_LOG).descending());
        List<ActivityLog> latestLogs = activityLogRepository.findAllByOrderByActionTimestampDesc(pageable);
        return latestLogs.stream()
                .map(this::convertToActivityLogResponse)
                .collect(Collectors.toList());
    }
    private ActivityLogResponse convertToActivityLogResponse(ActivityLog activityLog) {
        return ActivityLogResponse.builder()
                .activityLogId(activityLog.getActivityLogId())
                .actionType(activityLog.getActionType())
                .entityId(activityLog.getEntityId())
                .entityName(activityLog.getEntityName())
                .performedBy(activityLog.getPerformedBy())
                .actionTimestamp(activityLog.getActionTimestamp())
                .details(activityLog.getDetails())
                .build();
    }

    @Override
    public void update(Message m) {
        log.info("Observer Notified: " + m.getMessageContent());
        ActivityLogRequest activityLogRequest = ActivityLogRequest.builder()
                .actionType("Observer Notified - Process")
                .entityName(m.getMessageContent())
                .actionTimestamp(LocalDateTime.now())
                .details("Observed action: " +m.getMessageContent()
                        + " on date: " + LocalDateTime.now().toString())
                .build();
        logActivity(activityLogRequest);
    }
}
