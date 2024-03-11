package com.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("PasswordEncoder: BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        requests -> requests.requestMatchers("/order/*").hasRole("USER").anyRequest().permitAll())

                .formLogin((form) -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/to-my-page")
                        .usernameParameter("email").passwordParameter("password").failureUrl("/login?error")
                        .permitAll())
                .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/top").permitAll())
                .sessionManagement(session -> session
                        .sessionFixation().none()
                        .invalidSessionUrl("/login?invalid")
                        .maximumSessions(1).expiredUrl("/login?expired"));

        return http.build();
    }
}
