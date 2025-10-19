package com.vendo.notification_service.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private String from;

    private Properties properties;

    @Getter
    @Setter
    public static class Properties {

        private Mail mail;

        @Getter
        @Setter
        public static class Mail {

            private Transport transport;

            @Getter
            @Setter
            public static class Transport {

                private String protocol;

            }
        }
    }
}
