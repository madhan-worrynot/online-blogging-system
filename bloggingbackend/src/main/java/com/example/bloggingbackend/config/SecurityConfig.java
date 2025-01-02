package com.example.bloggingbackend.config;

import com.example.bloggingbackend.service.CustomUserDetailsService;
import com.example.bloggingbackend.filter.JwtAuthenticationFilter;
import com.example.bloggingbackend.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()  // Enable CORS
                .authorizeRequests(authz -> authz
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()  // Allow signup and login without authentication
                        .requestMatchers("/api/blogs/**").permitAll()  // Allow blog API routes without authentication
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()  // Allow fetching comments without authentication
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                        .anyRequest().authenticated()  // Require authentication for other requests
                )
                .csrf().disable()  // Disable CSRF protection as we're using JWT
                .httpBasic().disable();  // Disable HTTP basic authentication (optional)

        // Add the JWT filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();  // Your custom user details service
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil(), userDetailsService());
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();  // Make sure JwtUtil has @Component annotation for Spring to manage it
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://127.0.0.1:5500");  // Allow your frontend origin
        configuration.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, etc.)
        configuration.addAllowedHeader("*");  // Allow all headers
        configuration.setAllowCredentials(true);  // Allow credentials like cookies, JWT
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
