package com.fin.proj.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

        http.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/**.bo").authenticated() //해당 경로 보호
                .anyRequest().permitAll()) //아무런 보안없이 접근 가능
								
            .formLogin((formLogin) -> formLogin
	            .loginPage("/loginView.us")
	            .usernameParameter("uId")
	            .passwordParameter("uPwd")
	            .loginProcessingUrl("/login.us")
	            .defaultSuccessUrl("/"))
            
            .httpBasic(Customizer.withDefaults());

        return http.build();
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
