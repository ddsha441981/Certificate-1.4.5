package com.cwc.certificate.services;

import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.entities.enums.Gender;
import com.cwc.certificate.security.entities.enums.Role;
import com.cwc.certificate.security.repository.UserRepository;
import com.cwc.certificate.security.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
public class UserServiceImplTests {

    @Autowired
    private UserServiceImpl userServiceImpService;
    @Mock
    private UserRepository userRepository;

    @Test
    void userDetailsServiceTest(){
        when(userRepository.findByEmail(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(User.builder()
                        .firstName("Deendayal")
                        .lastName("Kumawat")
                        .email("kkumawat1111@gmail.com")
                        .dob(LocalDate.of(1993,06,01))
                        .gender(Gender.MALE)
                        .role(Role.ADMIN)
                        .password("Infinity@004500$P")
                        .build()));
        UserDetailsService userDetailsService = userServiceImpService.userDetailsService();
//        Assertions.assertEquals(userDetailsService);

    }
}
