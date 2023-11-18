package me.twometrue.eventmanager.configuration;

import me.twometrue.eventmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserService userService) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/popular").permitAll()
                        .requestMatchers("/history").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/register").permitAll()

                        .requestMatchers("/events/new").hasAnyRole("ADMIN", "EDITOR", "USER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().permitAll())

                        .formLogin(form -> form
                                .loginPage("/login")
                                .permitAll()
                        )
                        .logout(logout -> logout
                                .permitAll());

        return httpSecurity.build();
    }

}