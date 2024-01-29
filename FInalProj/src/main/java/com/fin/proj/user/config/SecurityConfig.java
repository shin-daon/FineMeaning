package com.fin.proj.user.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
	
	 @Bean
	 SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

        http.csrf((csrf) -> csrf.disable())
				.authorizeHttpRequests((requests) -> requests
                  .requestMatchers("/").authenticated() //해당 경로 보호
                  .anyRequest().permitAll()) //아무런 보안없이 접근 가능
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
		
    }
	 
	@Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
	
	@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
