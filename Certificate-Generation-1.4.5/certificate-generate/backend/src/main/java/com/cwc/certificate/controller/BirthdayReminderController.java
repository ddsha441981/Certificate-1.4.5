package com.cwc.certificate.controller;

import com.cwc.certificate.dto.response.BirthdayResponse;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.service.BirthdayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@RestController
@RequestMapping("/api/v3/security")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@Tag(name = "Get Birthday", description = "List of today birthday")
public class BirthdayReminderController {

    private final BirthdayService birthdayService;

    @GetMapping("/upcoming-birthdays")
    public ResponseEntity<List<BirthdayResponse>> getUpcomingBirthdays() {
        try {
            List<BirthdayResponse> upcomingBirthdays = birthdayService.getBirthdaysForToday();
            if (upcomingBirthdays == null || upcomingBirthdays.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(upcomingBirthdays, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching birthdays: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

