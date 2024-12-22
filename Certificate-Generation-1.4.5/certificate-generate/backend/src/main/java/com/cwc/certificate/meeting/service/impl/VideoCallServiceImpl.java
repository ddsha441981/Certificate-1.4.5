package com.cwc.certificate.meeting.service.impl;

import com.cwc.certificate.meeting.service.VideoCallingService;
import com.cwc.certificate.security.dao.response.VideoCallResponse;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoCallServiceImpl implements VideoCallingService {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RestTemplate restTemplate;
    @Override
    public VideoCallResponse getAdminDetailsAfterLogin(int userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()){
            VideoCallResponse videoCallResponse = mapToResponseUserToVideoCallResponse(user.get());

            return videoCallResponse;
        }else{
            throw new UsernameNotFoundException("not found");
        }
    }
    @Override
    public VideoCallResponse callVideoCallingApplicationByAdmin(VideoCallResponse videoCallResponse) {
        String url = "http://localhost:5000/auth/demo";
        ResponseEntity<VideoCallResponse> responseEntity = restTemplate.postForEntity(url, videoCallResponse, VideoCallResponse.class);
        VideoCallResponse response = responseEntity.getBody();
        return response;
    }

    private VideoCallResponse mapToResponseUserToVideoCallResponse(User user1) {
        VideoCallResponse loginResponse = VideoCallResponse.builder()
                .email(user1.getEmail())
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .password(user1.getPassword())
                .build();
        return loginResponse;
    }


}
