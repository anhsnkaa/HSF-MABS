package org.mabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/profile").authenticated() // Only authenticated users can access /profile
                        .requestMatchers("/home/admin").hasRole("ADMIN")
                        .requestMatchers("/accounts/**").hasRole("ADMIN")
                        .requestMatchers("/specialties/**").hasRole("ADMIN")
                        .requestMatchers("/doctors/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Use own login page(not spring security login default)
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().stream()
                                    .map(a -> a.getAuthority())
                                    .filter(a -> a.startsWith("ROLE_"))
                                    .findFirst().orElse("ROLE_PATIENT");
                            String redirectUrl = switch (role) {
                                case "ROLE_ADMIN" -> "/home/admin";
                                // add other cases
                                default -> "/home";
                            };
                            response.sendRedirect(redirectUrl);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true) // Xóa session trong server
                .clearAuthentication(true)  // Xóa context bảo mật
                .deleteCookies("JSESSIONID") // Xóa cookie của trình duyệt
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
