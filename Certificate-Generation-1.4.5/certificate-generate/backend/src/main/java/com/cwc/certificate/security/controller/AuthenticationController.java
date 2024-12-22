package com.cwc.certificate.security.controller;

import com.cwc.certificate.config.ratelimit.aop.RateLimited;
import com.cwc.certificate.security.dao.request.ForgetPasswordRequest;
import com.cwc.certificate.security.dao.request.SignUpRequest;
import com.cwc.certificate.security.dao.request.SigninRequest;
import com.cwc.certificate.security.dao.response.JwtAuthenticationResponse;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.service.AuthenticationService;
import com.cwc.certificate.security.service.UserService;
import com.cwc.certificate.utility.recaptcha.RecaptchaService;
import com.cwc.certificate.validations.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@RestController
@RequestMapping("/api/v3/security")
//@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@Tag(name = "Authentication" , description = "Login and Signup endpoints")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RecaptchaService recaptchaService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationService authenticationService, RecaptchaService recaptchaService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.recaptchaService = recaptchaService;
    }

    @Operation(summary = "Signup endpoints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<JwtAuthenticationResponse>> signup(@RequestBody SignUpRequest request) {
       try{
           JwtAuthenticationResponse signuped = authenticationService.signup(request);
           APIResponse<JwtAuthenticationResponse> apiResponse = APIResponse.<JwtAuthenticationResponse>builder()
                   .code(200)
                   .message("Successfully signup " + request.getEmail())
                   .responseData(signuped)
                   .build();
           log.info("Successfully signup {} ", request.getEmail());
           return new ResponseEntity<>(apiResponse,HttpStatus.OK);
       } catch (IllegalArgumentException e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request", e);
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
       }
    }


    //Verify Captcha
    @Operation(summary = "Verify Captcha before signup")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PostMapping("/verify-captcha")
    public ResponseEntity<Map<String, Object>> verifyCaptcha(@RequestBody Map<String, String> mapRequest,HttpServletRequest request) {
        String captchaResponse = mapRequest.get("captchaValue");
        String ip = request.getRemoteAddr();
        log.info("captcha ip is {} " ,ip);
        log.info("captcha captchaResponse is {} " ,captchaResponse);

        String captchaVerifyMessage = recaptchaService.verifyRecaptcha(ip, captchaResponse);
        if ( StringUtils.isNotEmpty(captchaVerifyMessage)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", captchaVerifyMessage);
            return ResponseEntity.badRequest()
                    .body(response);
        }
//        return ResponseEntity.ok().build();
        return ResponseEntity.ok(Map.of("success", "success"));
    }

    @Operation(summary = "Signing endpoints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @RateLimited
    @PostMapping("/signing")
    public ResponseEntity<APIResponse<JwtAuthenticationResponse>> signing(@RequestBody SigninRequest request) {
        try {
            JwtAuthenticationResponse authenticationResponse = authenticationService.signin(request);
            System.out.println("After JwtAuthenticationResponse " + authenticationResponse.getUserId() + "," + authenticationResponse.getToken());
            APIResponse<JwtAuthenticationResponse> apiResponse = APIResponse.<JwtAuthenticationResponse>builder()
                    .code(200)
                    .message("Login Successfully " + request.getEmail())
                    .responseData(authenticationResponse)
                    .build();
            log.info("Login Successfully {} ", request.getEmail());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }

    @Operation(summary = "Forget Password endpoints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @PostMapping("/forget_password")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        try {
            Optional<User> forgetPassword = this.userService.forgetPassword(forgetPasswordRequest);
            log.info("Forget password link send your email Successfully {} ", forgetPasswordRequest.getEmail());
            return new ResponseEntity<>(forgetPassword, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid request", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }


    @Operation(summary = "Change Password endpoints")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = AuthenticationController.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            }),
    })
    @PatchMapping("/change_password/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable("userId") Integer userId,@RequestBody ForgetPasswordRequest forgetPasswordRequest){
       this.userService.changePassword(userId, forgetPasswordRequest);
        log.info("Password change Successfully {} ", forgetPasswordRequest.getEmail());
        return new ResponseEntity<>("Your password successfully changed",HttpStatus.OK);
    }

    @GetMapping("/get/logged/user/details/{userId}")
    public ResponseEntity<User> getLoggedInUserDetails(@PathVariable("userId") Integer userId){
        User loggedInUserDetails = this.userService.getLoggedInUserDetails(userId);
        return new ResponseEntity<>(loggedInUserDetails, HttpStatus.OK);
    }


}
