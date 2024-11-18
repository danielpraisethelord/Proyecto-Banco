package com.ulsa.oaxaca.edu.proyecto_banco.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ulsa.oaxaca.edu.proyecto_banco.security.filter.JwtAuthenticationFilter;
import com.ulsa.oaxaca.edu.proyecto_banco.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

        @Autowired
        private AuthenticationConfiguration authenticationConfiguration;

        @Bean
        AuthenticationManager authenticationManager() throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aplicar
                                                                                                   // configuración CORS
                                                                                                   // aquí
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                                                "/swagger-ui.html", "/swagger-resources/**",
                                                                "/webjars/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()),
                                                UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(new JwtValidationFilter(authenticationManager()),
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.addAllowedOrigin("http://localhost:5173");
                config.addAllowedOrigin("https://banco-app-ulsa.netlify.app:5173");
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }

}
