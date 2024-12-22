package com.cwc.certificate.security.service.impl;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.model.EmailDetails;
import com.cwc.certificate.driven.rabbitmq.RabbitMQConfig;
import com.cwc.certificate.security.dao.request.ForgetPasswordRequest;
import com.cwc.certificate.security.entities.User;
import com.cwc.certificate.security.repository.UserRepository;
import com.cwc.certificate.security.service.UserService;
import com.cwc.certificate.utility.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;
    private final RabbitTemplate rabbitTemplate;



    @Autowired
    public UserServiceImpl(UserRepository userRepository, SendEmailService sendEmailService,RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.sendEmailService = sendEmailService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this username " + username));
    }

    @Override
    public Optional<User> forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        String email = forgetPasswordRequest.getEmail();
        String resetPasswordLink = forgetPasswordRequest.getChangePasswordUrl();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            int userId = user.get().getUserId();
            String firstName =  user.get().getFirstName();
            String lastName = user.get().getLastName();
            String fullName = firstName +" "+ lastName;
            //remove default value from url
            String modifiedResetPasswordLink = resetPasswordLink.substring(0, resetPasswordLink.lastIndexOf("/")) + "/" + userId;
            String message = String.format(ConstantValue.FORGET_PASSWORD_LINK_MESSAGE, fullName,modifiedResetPasswordLink);
            System.out.println(message);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(email)
                    .subject(ConstantValue.FORGOT_PASSWORD_EMAIL_SUBJECT)
                    .msgBody(message)
                    .build();
            log.info("Sending email details to queue: {} " , emailDetails);
            // Send the message to RabbitMQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, emailDetails);
            //send email
            //sendEmailService.sendSimpleMail(emailDetails);
            return user;
        }else{
            throw new UsernameNotFoundException("User not found with this email");
        }
    }

    @Override
    public User changePassword(Integer userId, ForgetPasswordRequest forgetPasswordRequest) {
        User userFound = this.userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this user Id {} " + userId));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = forgetPasswordRequest.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        userFound.setPassword(encodedPassword);
        return userRepository.save(userFound);
    }

    @Override
    public User getLoggedInUserDetails(int userId) {
        return this.userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found with this user Id : {} " + userId));
    }


}
