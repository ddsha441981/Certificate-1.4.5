package com.cwc.certificate.service;

import com.cwc.certificate.dto.request.ActivityLogRequest;
import com.cwc.certificate.dto.response.ActivityLogResponse;
import com.cwc.certificate.observer.Observer;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/09/02
 */
public interface ActivityLogService extends Observer {
     void logActivity(ActivityLogRequest activityLogRequest);
     List<ActivityLogResponse> getLogActivityDetails();
}
