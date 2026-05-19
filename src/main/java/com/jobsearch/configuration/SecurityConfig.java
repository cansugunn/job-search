package com.jobsearch.configuration;

import com.jobsearch.security.converter.SupabaseJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final SupabaseJwtConverter jwtConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s ->
                               s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/v1/jobs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/jobs/autocomplete").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/cities").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/cities/autocomplete").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/countries").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/companies").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/towns").permitAll()
            .requestMatchers("/v3/api-docs/**",
                             "/swagger-ui/**",
                             "/swagger-ui.html")
            .permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 ->
                                  oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));
    return http.build();
  }
}
