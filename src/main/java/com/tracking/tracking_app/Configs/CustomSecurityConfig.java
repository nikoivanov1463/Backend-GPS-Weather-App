package com.tracking.tracking_app.Configs;

import com.tracking.tracking_app.Filters.JWTFilter;
import com.tracking.tracking_app.Services.SecurityConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {
    @Autowired
    JWTFilter jwtFilter;

    private final SecurityConfigService securityConfigService;

    public CustomSecurityConfig(SecurityConfigService securityConfigService) {
        this.securityConfigService = securityConfigService;
    }

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .securityMatcher("/web/**", "/js/**", "/css/**", "/favicon.ico")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.configure(httpSecurity)); // enable CSRF (default)
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
//        return httpSecurity.csrf(csrf -> csrf.disable())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("api/login", "api/register", "api/session-check", "/error").permitAll()
//                        .requestMatchers("api/logout", "api/save-all-markers", "api/delete-selected-marker").authenticated()
//                        .anyRequest().authenticated()
//                )
//                .logout(logout -> logout.permitAll())
//                .build();

        return httpSecurity.csrf((csrf) -> csrf.disable()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).authorizeHttpRequests(request -> request.requestMatchers("/api/login", "/api/register", "/api/session-check", "/api/logout", "/error", "/api/change").permitAll().anyRequest().authenticated()).logout(logout -> logout.permitAll()).
                build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(securityConfigService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(12, 20, 1, 2, 1);
    }
}
