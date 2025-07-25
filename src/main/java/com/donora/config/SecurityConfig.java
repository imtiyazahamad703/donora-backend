package com.donora.config;

import com.donora.entity.User;
import com.donora.jwt.JwtAuthFilter;
import com.donora.repository.UserRepository;
import com.donora.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /*@Bean   // this is for httpBasic Security and for JWT we will create separate UserDetailsService class
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name()) // Setting roles from the User entity
                    .build();
        };
    } */

    // Encode passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //need to create this because it's internally exits but if you want to make changes then only
    // Authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Main security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // this is only if you want jwt no need for basic security bcoz jwt is STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Optional
                .exceptionHandling(eh -> eh.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                ))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "api/public/**"
                        ).permitAll()

                        // Dashboard routes by role
                        .requestMatchers("/api/dashboard/admin").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/ngo").hasRole("NGO")
                        .requestMatchers("/api/dashboard/user").hasRole("USER")

                        // NGO routes
                        .requestMatchers("/api/ngos/**").hasRole("NGO")
                        .requestMatchers("/api/ngos/profile").hasRole("NGO")
                        .requestMatchers("/api/ngos/needs/**").hasRole("NGO")

                        // User routes
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/users/item-donations").hasRole("USER")//no need optional


                        // ✅ NEW: Business routes
                        .requestMatchers("/api/business/**").hasRole("BUSINESS")
                        .requestMatchers("/api/business/food-donations").hasRole("BUSINESS")

                        // Admin fallback
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        //all emegency need....
                        .requestMatchers("/api/public/**").permitAll()

                        .anyRequest().authenticated()
                );

                // .httpBasic(Customizer.withDefaults()); //this is for Basic security

                // this is for jwt
                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
