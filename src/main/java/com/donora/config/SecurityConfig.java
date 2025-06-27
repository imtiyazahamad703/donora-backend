package com.donora.config;

import com.donora.entity.User;
import com.donora.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
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
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password"
                        ).permitAll()

                        // Dashboard routes by role
                        .requestMatchers("/api/dashboard/admin").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/ngo").hasRole("NGO")
                        .requestMatchers("/api/dashboard/user").hasRole("USER")

                        // NGO routes
                        .requestMatchers("/api/ngos/profile").hasRole("NGO")
                        .requestMatchers("/api/ngos/needs/**").hasRole("NGO")
                        .requestMatchers("/api/ngos/**").hasRole("NGO")

                        // User routes
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/users/item-donations").hasRole("USER")//no need optional


                        // ✅ NEW: Business food donation endpoint
                        .requestMatchers("/api/business/food-donations").hasRole("BUSINESS")

                        // Admin fallback
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        //all emegency need....
                        .requestMatchers("/api/public/**").permitAll()


                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.csrf(csrf -> csrf.disable()).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Optional: expose AuthenticationManager if needed
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
