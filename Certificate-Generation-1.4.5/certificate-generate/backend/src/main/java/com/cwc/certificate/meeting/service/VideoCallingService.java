package com.cwc.certificate.meeting.service;

import com.cwc.certificate.security.dao.response.JwtAuthenticationResponse;
import com.cwc.certificate.security.dao.response.VideoCallResponse;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

public interface VideoCallingService {


    VideoCallResponse getAdminDetailsAfterLogin(int userId);
    VideoCallResponse callVideoCallingApplicationByAdmin(VideoCallResponse videoCallResponse);


}
