package com.cwc.certificate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail(ConstantValue.EMAIL_ADDRESS);
        contact.setName(ConstantValue.DEVELOPER_NAME);
        contact.setUrl(ConstantValue.SET_URL);

        Server localServer = new Server();
        localServer.setUrl(ConstantValue.SET_LOCALHOST_URL);
        localServer.setDescription(ConstantValue.SET_LOCAL_DESCRIPTION);


        Server productionServer = new Server();
        productionServer.setUrl(ConstantValue.SET_PRODUCTION_URL);
        productionServer.setDescription(ConstantValue.SET_PRODUCTION_DESCRIPTION);

        License mitLicense = new License()
                .name(ConstantValue.SET_MIT_LICENSE)
                .url(ConstantValue.SET_MIT_LICENSE_URL);

        Info info = new Info()
                .title(ConstantValue.SET_INFO_TITLE)
                .contact(contact)
                .version(ConstantValue.SET_APP_VERSION)
                .description(ConstantValue.SET_INFO_DESCRIPTION)
                .termsOfService(ConstantValue.SET_TERMS_OF_SERVICE)
                .summary(ConstantValue.SET_INFO_SUMMERY)
                .license(mitLicense);


        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(ConstantValue.TOKEN_TYPE_HEADER_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(ConstantValue.TOKEN_TYPE_HEADER_NAME,new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme(ConstantValue.TOKEN_TYPE_SCHEME_NAME)
                                        .bearerFormat(ConstantValue.TOKEN_TYPE_BEARER_AUTH)))
                .servers(List.of(localServer, productionServer));
    }
}
