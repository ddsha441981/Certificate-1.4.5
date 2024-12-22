package com.cwc.certificate.security.service;

import com.cwc.certificate.security.dao.request.ForgetPasswordRequest;
import com.cwc.certificate.security.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

public interface UserService {
    UserDetailsService userDetailsService();
    public Optional<User> forgetPassword(ForgetPasswordRequest forgetPasswordRequest);
    User changePassword(Integer userId, ForgetPasswordRequest forgetPasswordRequest);

    User getLoggedInUserDetails(int userId);
}
