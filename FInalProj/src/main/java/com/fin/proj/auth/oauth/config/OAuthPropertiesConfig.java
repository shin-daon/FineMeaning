package com.fin.proj.auth.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth")
public class OAuthPropertiesConfig {

    @Bean
    @ConfigurationProperties(prefix = "kakao")
    public Kakao kakao() {
        return new Kakao();
    }
}
