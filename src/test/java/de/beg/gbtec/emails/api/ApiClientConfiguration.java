package de.beg.gbtec.emails.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfiguration {

    @Bean
    public EmailControllerApiClient emailControllerApiClient() {
        return new EmailControllerApiClient();
    }

}
