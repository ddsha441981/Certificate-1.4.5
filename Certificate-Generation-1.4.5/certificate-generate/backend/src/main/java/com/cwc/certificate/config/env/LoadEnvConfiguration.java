package com.cwc.certificate.config.env;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadEnvConfiguration {
    public static void loadENVPropertiesConfiguration(){
        log.info("env configuration loading from configuration file");
        Dotenv dotenv = Dotenv.load();
        String emailUsername = dotenv.get("EMAIL_USERNAME");
        String emailPassword = dotenv.get("EMAIL_PASSWORD");
//        String tokenSigningKey = dotenv.get("TOKEN_SIGNING_KEY");
        String openViduSecret = dotenv.get("OPENVIDU_SECRET");
        String postgresPassword = dotenv.get("DATABASE_PASSWORD");
        String googleRecaptchaSecretKey = dotenv.get("GOOGLE_RECAPTCHA_KEY");

        System.setProperty("spring.mail.username", emailUsername);
        System.setProperty("spring.mail.password", emailPassword);
//        System.setProperty("token.signing.key", tokenSigningKey);
        System.setProperty("openvidu.secret", openViduSecret);
        System.setProperty("spring.datasource.password", postgresPassword);
        System.setProperty("google.recaptcha.secret", googleRecaptchaSecretKey);
        log.info("env configuration loaded successfully and configure inside properties file");
    }
}
