package com.cwc.certificate.meeting.controller;


import com.cwc.certificate.meeting.service.VideoCallingService;
import com.cwc.certificate.security.dao.response.VideoCallResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v4/communicate")
public class VideoCallingController {

    @Autowired
    private final VideoCallingService videoCallingService;


    @GetMapping("/admin/details/{userId}")
    public ResponseEntity<VideoCallResponse> adminLoginDetailsWithCredential(@PathVariable("userId") Integer userId){
        VideoCallResponse adminDetailsAfterLogin = this.videoCallingService.getAdminDetailsAfterLogin(userId);
        return new ResponseEntity<>(adminDetailsAfterLogin,HttpStatus.OK);
    }

    //Call Video Calling API Project
    @PostMapping("/admin/video-link/loginDetails")
    public ResponseEntity<?> adminLoginDetailsWithCredential(@RequestBody VideoCallResponse videoCallResponse){

        VideoCallResponse callResponseLink = this.videoCallingService.callVideoCallingApplicationByAdmin(videoCallResponse);
        System.out.println(callResponseLink);
        return new ResponseEntity<>(callResponseLink,HttpStatus.OK);
    }
}
