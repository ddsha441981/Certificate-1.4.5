package com.cwc.certificate.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@RestController
@RequestMapping("/api/v3/resource")
@RequiredArgsConstructor
@Tag(name = "Authorization" , description = "Check endpoints after login")
public class AuthorizationController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Here is your resource");
    }
}
