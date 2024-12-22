package com.cwc.certificate.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.cwc.certificate.config.ConstantValue;
import com.cwc.certificate.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/07/09
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_1)
                                .authenticated())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_2)
                                .authenticated())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_4)
                                .authenticated())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_5)
                                .permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_6)
                                .permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.SWAGGER_WHITELIST)
                                .permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.ACTUATOR_WHITELIST)
                                .permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(ConstantValue.API_BASE_URL_VERSION_3)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(STATELESS)
                )
                .authenticationProvider(authenticationProvider()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}