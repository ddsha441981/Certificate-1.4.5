package com.cwc.certificate.security.service.impl;

import com.cwc.certificate.security.dao.request.SignUpRequest;
import com.cwc.certificate.security.dao.request.SigninRequest;
import com.cwc.certificate.security.dao.response.JwtAuthenticationResponse;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.entities.enums.Role;
import com.cwc.certificate.security.repository.UserRepository;
import com.cwc.certificate.security.service.AuthenticationService;
import com.cwc.certificate.security.service.JwtService;
import com.cwc.certificate.utility.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtService jwtService;

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        LocalDate dob = request.getDob();
        String formatDate = DateUtils.formatDate(dob);
        LocalDate localDate = DateUtils.parseDate(formatDate);

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .dob(localDate)
                .gender(request.getGender())
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    @Override
    @SneakyThrows
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Check if token is available inside Redis
        JwtAuthenticationResponse cachedResponse = getCachedResponse(request.getEmail());
        if (cachedResponse != null) {
            // Token is already cached, return it
            log.info("Token found inside caching");
            return cachedResponse;
        }

        // User isn't found in Redis cache, generate new token
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        log.info("Token not found inside caching, generating new one");
        var jwt = jwtService.generateToken(user);
        Role role = user.getRole();
        var userId = user.getUserId();

        JwtAuthenticationResponse authenticationResponse = JwtAuthenticationResponse.builder()
                .token(jwt)
                .role(role)
                .userId(userId)
                .build();

        // Cache the token in Redis with a TTL 60 min
        cacheToken(request.getEmail(), authenticationResponse);
        return authenticationResponse;
    }

    private void cacheToken(String email, JwtAuthenticationResponse response) {
        try {
            String responseJson = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set("token:" + email, responseJson, Duration.ofMinutes(60));
        } catch (JsonProcessingException e) {
            log.error("Error in cached token",e);

        }
    }

    private JwtAuthenticationResponse getCachedResponse(String email) {
        String cachedResponseJson = redisTemplate.opsForValue().get("token:" + email);
        if (cachedResponseJson != null) {
            try {
                return objectMapper.readValue(cachedResponseJson, JwtAuthenticationResponse.class);
            } catch (JsonProcessingException e) {

                log.error("Error occurred : ", e);
            }
        }
        return null;
    }




//    public JwtAuthenticationResponse signin(SigninRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//
//
//        // Check if token is available inside Redis
//        String cachedTokenJson = getCachedToken(request.getEmail());
//        if (cachedTokenJson != null) {
//            // Token is already cached, return it
//            return parseJwtAuthenticationResponse(cachedTokenJson);
//        }
//
//
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
//
//        var jwt = jwtService.generateToken(user);
//        Role role = user.getRole();
//        var userId = user.getUserId();
//
//        JwtAuthenticationResponse authenticationResponse = JwtAuthenticationResponse.builder()
//                .token(jwt)
//                .role(role)
//                .userId(userId)
//                .build();
//        // Cache the token in Redis with a TTL 60 min
//        cacheToken(request.getEmail(), authenticationResponse);
//
//        return authenticationResponse;
//    }
//
//
//    private void cacheToken(String email, JwtAuthenticationResponse response) {
//        try {
//            String tokenJson = objectMapper.writeValueAsString(response);
//            redisTemplate.opsForValue().set("token:" + email, tokenJson, Duration.ofMinutes(60));
//        } catch (JsonProcessingException e) {
//            // Handle JSON serialization error
//            e.printStackTrace();
//        }
//    }
//
//    private JwtAuthenticationResponse parseJwtAuthenticationResponse(String tokenJson) {
//        try {
//            return objectMapper.readValue(tokenJson, JwtAuthenticationResponse.class);
//        } catch (JsonProcessingException e) {
//            // Handle JSON deserialization error
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private String getCachedToken(String email) {
//        return redisTemplate.opsForValue().get("token:" + email);
//    }

}
