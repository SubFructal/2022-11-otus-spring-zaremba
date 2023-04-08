package ru.otus.homework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .antMatchers("/login").permitAll()
                        .antMatchers("/", "/authors", "/genres",
                                "/books-by-author", "/books-by-genre").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/delete", "/delete-all", "/edit",
                                "/add", "/h2-console").hasRole("ADMIN")
                )
                .formLogin()
                .and()
                .rememberMe().key("appSecretKey").tokenValiditySeconds(60 * 10)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
