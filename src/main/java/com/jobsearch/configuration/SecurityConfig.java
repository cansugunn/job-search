package com.jobsearch.configuration;

import com.jobsearch.security.converter.SupabaseJwtConverter;
import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        .cors(c -> c.configurationSource(corsConfigurationSource()))
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
