package com.cwc.certificate.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.dto.response.BirthdayResponse;
import com.cwc.certificate.repository.CertificateRepository;
import com.cwc.certificate.security.entities.enums.Gender;
import com.cwc.certificate.service.BirthdayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Slf4j
@Service
public class BirthdayServiceImpl implements BirthdayService {

    private final CertificateRepository certificateRepository;

    @Autowired
    public BirthdayServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<BirthdayResponse> getBirthdaysForToday() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        List<Object[]> userList = certificateRepository.findCandidateDetailsByDobMonthAndDobDay(month, day);
        StringBuilder birthdayList = new StringBuilder(ConstantValue.DEFAUL_BIRTHDAY_LIST);

        userList.forEach(userData -> {
            if (userData.length >= 2) {
                String candidateName = (String) userData[0];
                birthdayList.append(candidateName).append(", ");
            }
        });
        if (birthdayList.length() > 0) {
            birthdayList.setLength(birthdayList.length() - 2);
        }
        log.info(birthdayList.toString());

        return userList.stream()
                .map(userData -> {
                    String candidateName = (String) userData[0];
                    String email = (String) userData[1];
                    Gender gender = (Gender) userData[2];

                    return BirthdayResponse.builder()
                            .candidateName(candidateName)
                            .candidateEmail(email)
                            .dob(today)
                            .gender(gender)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
