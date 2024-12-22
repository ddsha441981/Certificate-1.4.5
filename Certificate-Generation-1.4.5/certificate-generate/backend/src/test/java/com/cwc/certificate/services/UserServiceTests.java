package com.cwc.certificate.services;

import com.cwc.certificate.security.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Disabled
    @Test
    public void testMethod(){
        assertNotNull(userRepository.findByEmail("ram"));
    }
}
