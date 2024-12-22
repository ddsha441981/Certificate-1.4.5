package com.cwc.certificate.security.service;


import com.cwc.certificate.security.dao.request.SignUpRequest;
import com.cwc.certificate.security.dao.request.SigninRequest;
import com.cwc.certificate.security.dao.response.JwtAuthenticationResponse;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */




public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

//    void logOut();

    //Crud Operation

}
