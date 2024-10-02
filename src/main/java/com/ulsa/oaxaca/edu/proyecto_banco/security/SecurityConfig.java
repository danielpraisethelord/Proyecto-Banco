// package com.ulsa.oaxaca.edu.proyecto_banco.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// .csrf().disable() // Desactiva CSRF (opcional, si no estás usando
// formularios).
// .authorizeHttpRequests((requests) -> requests
// .anyRequest().permitAll() // Permite todas las solicitudes sin
// // autenticación.
// )
// .formLogin().disable() // Desactiva el login por formulario.
// .httpBasic().disable(); // Desactiva autenticación básica (opcional).
// return http.build();
// }

// // @Bean
// // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// // Exception {
// // http
// // .authorizeHttpRequests(authorize -> authorize
// // .requestMatchers("/api/**").permitAll()
// // .anyRequest().authenticated())
// // .formLogin(formLogin -> formLogin
// // .loginPage("/login")
// // .permitAll())
// // .rememberMe(Customizer.withDefaults());

// // return http.build();
// // }
// }
